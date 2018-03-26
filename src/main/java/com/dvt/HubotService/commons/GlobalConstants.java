package com.dvt.HubotService.commons;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.dvt.HubotService.business.main.controller.MyController;
import com.dvt.HubotService.business.main.dto.AuthDTO;
import com.dvt.HubotService.business.main.dto.DictDTO;
import com.dvt.HubotService.business.main.dto.DictTypeDTO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.commons.utils.XmlUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.time.nlp.TimeNormalizer;



public class GlobalConstants {
	public static final String PAGE_TEST = "test/main";
	public static final String PAGE_AUTOCODE = "ui/main";
	
	public static final Map<String,HubotInterface> interfaces = Maps.newHashMap();
	public static final Map<String,String> baiduUnitErrKv = Maps.newHashMap();
	public static TimeNormalizer normalizer;
	
	public static AuthDTO myAuth = new AuthDTO(); 
	public static Map<String,String> aiSessions = Maps.newHashMap(); 
	public static Map<String,String> sceneWithUserSessions = Maps.newHashMap();//session缓存，以sceneid_userid为key
	public static Map<String,DictTypeDTO> codeDicts = Maps.newHashMap(); //字典缓存
	
	private static void readHubotInterfaceConfig(List<Node> hiFields){
		for (Node _hi : hiFields) {
			Element hi = (Element)_hi;
			String interfaceKey = hi.attributeValue("name");//接口的唯一标识
			Boolean executable = Boolean.valueOf(hi.attributeValue("executable"));
			if(executable){//标注有效
				System.out.println("装载接口"+interfaceKey);
				HubotInterface O = new HubotInterface();
				List<Element> hiel = hi.elements();
				for (Element element : hiel) {
					if(element.getName().equals("description")){
						O.setDescription(element.getText());
					}else if(element.getName().equals("answer")){
						O.setAnswer(element.getText());
					}else if(element.getName().equals("entryUrl")){
						O.setEntryUrl(element.getText());
					}else if(element.getName().equals("dictUrl")){
						O.setDictUrl(element.getText());
					}else if(element.getName().equals("postObject")){
						List<Element> els = element.elements();//处理待发送参数
						if(els.size()==1){
							Element belowPostObjectels = els.get(0);
							if("list".equals(belowPostObjectels.getName())){
								O.setPostObjectList(O.arrangeParamsArray(belowPostObjectels));
							}else if("object".equals(belowPostObjectels.getName())){
								O.setPostObject(O.arrangeParamsObject(belowPostObjectels));
							}
						}
					}
				}
				interfaces.put(interfaceKey, O);
			}
		}
	}
	
	static{
		Properties prop = new Properties();
		
		//百度AI的对话单元 与 被调系统的关联关系配置
		if(interfaces.isEmpty()){
			//解析Xml
			String filePath;
			try {
				filePath = GlobalConstants.class.getClassLoader().getResource("hubot.xml").toURI().getPath();
				Document document = XmlUtils.read(filePath);
				//对于直接在这里配置的接口
				List<Node> hiFields = XmlUtils.XpathQuery(document, "/configuration/hubot-interface");
				readHubotInterfaceConfig(hiFields);
				//对于要导入的其他配置文件
				List<Node> himFields = XmlUtils.XpathQuery(document, "/configuration/hubot-import");
				for (Node _him : himFields) {
					Element him = (Element)_him;
					String resourceFileName = him.attributeValue("resource");
					String resourceFilePath = GlobalConstants.class.getClassLoader().getResource(resourceFileName).toURI().getPath();
					Document importDocument = XmlUtils.read(resourceFilePath);
					List<Node> himConfigFields = XmlUtils.XpathQuery(importDocument, "/configuration/hubot-interface");
					readHubotInterfaceConfig(himConfigFields);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//百度api的错误状态
		if(baiduUnitErrKv.isEmpty()){
			prop = new Properties();
			try {
				prop.load(GlobalConstants.class.getClassLoader().getResourceAsStream("error.properties"));
				Enumeration en = prop.propertyNames();
		        while (en.hasMoreElements()) {
		            String key=(String) en.nextElement();
		            String value = prop.getProperty(key);
		            baiduUnitErrKv.put(key, value);
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			URL url = TimeNormalizer.class.getResource("/TimeExp.m");
			System.out.println("加载time解析资源：" + url.toURI().toString());
			normalizer= new TimeNormalizer(url.toURI().toString());
			normalizer.setPreferFuture(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
