package com.dvt.HubotService.business.main.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dvt.HubotService.business.main.dto.DictDTO;
import com.dvt.HubotService.business.main.dto.FormDTO;
import com.dvt.HubotService.business.main.service.DictService;
import com.dvt.HubotService.business.main.service.SampleService;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.utils.XmlRpcUtils;
import com.dvt.HubotService.commons.utils.XmlUtils;
import com.dvt.HubotService.commons.utils.ZipUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@Controller
@RequestMapping("/ui")
public class UiController {
	private static final Logger logger = LoggerFactory.getLogger(UiController.class);
	private static final String LINE_SEP = System.getProperty("line.separator");
	private static final String PATH_SEP = File.separator;
	
	private static List<String> condition_string_xml;
	private static List<String> condition_selection_xml;
	private static List<String> condition_many2one_xml;
	private static List<String> condition_date_xml;
	private static List<String> condition_datetime_xml;
	private static List<String> config_xml;
	private static boolean hasInitTemplate = Boolean.FALSE;
	
	private String odooDictUrl = "http://139.129.225.173:7060/rpc/fields_get";
	
	@Autowired
	private SampleService sampleService;
	@Autowired
	private DictService dictService;
	
	@RequestMapping
	public String init(HttpServletRequest request) {
		String templatePath = request.getSession().getServletContext().getRealPath("")+"template";
		if(!hasInitTemplate){
			initTemplate(request, templatePath); //初始化模板
		}
		
		return GlobalConstants.PAGE_AUTOCODE;
	}
	
	private void initTemplate(HttpServletRequest request,String templatePath){
		try {
			condition_string_xml = FileUtils.readLines(new File(templatePath+PATH_SEP+"hubot-condition-string.xml"));
			condition_selection_xml = FileUtils.readLines(new File(templatePath+PATH_SEP+"hubot-condition-selection.xml"));
			condition_many2one_xml = FileUtils.readLines(new File(templatePath+PATH_SEP+"hubot-condition-many2one.xml"));
			condition_date_xml = FileUtils.readLines(new File(templatePath+PATH_SEP+"hubot-condition-date.xml"));
			condition_datetime_xml = FileUtils.readLines(new File(templatePath+PATH_SEP+"hubot-condition-datetime.xml"));
			config_xml = FileUtils.readLines(new File(templatePath+PATH_SEP+"hubot-interface.xml"),"UTF-8");
			hasInitTemplate = Boolean.TRUE;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/download")
	public void download(@ModelAttribute FormDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer uid = 1;
		Integer action_id = dto.getAction_id(); 
		String odooUrl = dto.getOdooUrl();
		String db = dto.getDb();
		String pwd = dto.getPwd();
		String model = dto.getModelName();
		String intent = dto.getIntent_id();
		//词槽与key的对应关系 组装成map
		List<String> nameSlots = Arrays.asList(dto.getName_slots().split("\r\n"));
		final Map<String,String> nameSlotsMap = Maps.newHashMap();
		nameSlots.stream().forEach((String e) -> {
			String[] strs = e.replace("：", ":").split(":") ;
			nameSlotsMap.put(strs[0], strs[1]);
		});
		
		String url = String.format("%s/xmlrpc/2/object", odooUrl);
		
		Map context = ImmutableMap.of(
			"lang", "zh_CN",
			"tz", "Asia/Hong_Kong",
			"uid", uid,
			"search_default_no_share", 1,
			"params", ImmutableMap.of("action",action_id)
		);
		
		Map options = ImmutableMap.of(
			"action_id", action_id,
			"load_fields", true,
			"toolbar", true,
			"load_filters", true
		);
		
		//odoo模型字段类型 和 这边能处理的类型 的对应关系
		Map<String,String> odooFieldType = new ImmutableMap.Builder<String, String>()
									.put("char,text,html,int,float,monetary", "string")
									.put("selection","selection")
									.put("many2one","many2one")
									.put("date","date")
									.put("datetime","datetime")
									.build(); 
		
		List views = Lists.newArrayList();
		if(dto.getForm_id()!=null)
			views.add(Lists.newArrayList(dto.getForm_id(),"form"));
		if(dto.getTree_id()!=null)
			views.add(Lists.newArrayList(dto.getTree_id(),"list"));
		if(dto.getSearch_id()!=null)
			views.add(Lists.newArrayList(dto.getSearch_id(),"search"));
		
		Map loadViews = (HashMap) XmlRpcUtils.getXMLRPC(url, "execute_kw", Arrays.asList(db, uid, pwd, model,"load_views",
				Lists.newArrayList(),new ImmutableMap.Builder<String, Object>()
									 .put("context", context)
									 .put("options", options)
									 .put("views", views).build()
		));
		Map fieldsViews = (HashMap)loadViews.get("fields_views");
		String xmlPath = outputXml(request, fieldsViews);//落地模型视图的xml
		
		//获取功能名称
		List<Node> formFields = XmlUtils.XpathQuery(XmlUtils.read(xmlPath + PATH_SEP + "form.xml"), "/form");
		Element e_form = (Element)formFields.get(0);
		String thing = (String)(e_form.attributeValue("string"));
		System.out.println("功能名称："+ thing);
		
		//生成hubot配置文件 和 自动对话样本
		StringBuffer templateConditionConfig = new StringBuffer("");
		
		//生成对话样本
		List<String> sampleOriginList = Lists.newArrayList();
		sampleOriginList.add(thing);
		sampleOriginList.add(intent);
		
		Map form = (HashMap)fieldsViews.get("form");
		Map fields = (HashMap)form.get("fields");
		Set<String> keySet = fields.keySet();
		for (String key : keySet) {
			Map<String,Object> field = (HashMap)fields.get(key);
			String content = "";
			String name = (String)field.get("string");
			String type = (String)field.get("type");//这个type是odoo的模型字段的类型，需要再加工
			for (String e : odooFieldType.keySet()) {
				type = e.contains(type)?odooFieldType.get(e):type;
			}
			
			String slot = "";
			if(nameSlotsMap.containsKey(name)){
				slot = nameSlotsMap.get(name);
				content = slot + "|" + name ;
				
				switch (type) {
					case "string":{
						content += "|" + type;
						for (String line : condition_string_xml) {
							String _line = new String(line);
							_line = _line.replace("${key}", key);
							_line = _line.replace("${name}", name);
							_line = _line.replace("${slot}", slot);
							templateConditionConfig.append(_line);
						}
						
						List<String> fieldParam = ImmutableList.of(key);
						List<String> groupby = ImmutableList.of(key);
						List domain = ImmutableList.of(ImmutableList.of("state","not in",ImmutableList.of("draft","sent","cancel")));
						Map contextParam = new ImmutableMap.Builder<String, Object>()
								.put("group_by", key)
								.put("lang","zh_CN")
								.put("tz","Asia/Hong_Kong")
								.put("uid", uid)
								.put("params",new ImmutableMap.Builder<String, Integer>().put("action", action_id).build())
								.build(); 
						Object[] fieldGroups = (Object[])XmlRpcUtils.getXMLRPC(url, "execute_kw", Arrays.asList(db, uid, pwd, model,"read_group",
								Lists.newArrayList(),new ImmutableMap.Builder<String, Object>()
													 .put("fields", fieldParam)
													 .put("groupby", groupby)
													 .put("domain", domain)
													 .put("context",contextParam).build()
						));
						if(fieldGroups!=null){
							String StringStr = "";
							List fieldGroupsList = Arrays.asList(fieldGroups);
							int count = 0;
							for (Object object : fieldGroupsList) {
								Map fieldSampleMap = (HashMap)object;
								StringStr += ((String)fieldSampleMap.get(key)).trim() + ",";
								count++;
								if(count>5){
									break;
								}
							}
							StringStr = StringStr.substring(0,StringStr.length()-1);
							content += "|" + StringStr;
						}
						
						break;
					}
					case "selection":{
						content += "|" + type;
						for (String line : condition_selection_xml) {
							String _line = new String(line);
							_line = _line.replace("${key}", key);
							_line = _line.replace("${name}", name);
							_line = _line.replace("${slot}", slot);
							templateConditionConfig.append(_line);
						}		
						
						Object[] selections = (Object[]) field.get("selection");
						String selectionStr = "";
						for (int i = 0; i < selections.length; i++) {
							Object[] e = (Object[])selections[i];
							selectionStr += ((String)e[1]).trim() + ",";
						}
						selectionStr = selectionStr.substring(0,selectionStr.length()-1);
						content += "|" + selectionStr;
						break;
					}
					case "many2one":{
						content += "|" + "selection";
						String relation = (String)field.get("relation");
						for (String line : condition_many2one_xml) {
							String _line = new String(line);
							_line = _line.replace("${key}", key);
							_line = _line.replace("${name}", name);
							_line = _line.replace("${relation}", relation);
							_line = _line.replace("${slot}", slot);
							templateConditionConfig.append(_line);
						}
						
						List<DictDTO> sampleValues = dictService.getDictByType(odooDictUrl, relation, model, key);
						//去除空值
						sampleValues = sampleValues.stream().filter(sv -> StringUtils.isNotBlank(sv.getDictKey())).collect(Collectors.toList());
						//去重
						sampleValues = sampleValues.stream().collect(Collectors.collectingAndThen(
								Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DictDTO::getDictKey))), ArrayList::new));
						String sampleValuesStr = "";
						for (int i = 0; i < sampleValues.size(); i++) {
							sampleValuesStr += sampleValues.get(i).getDictKey().replace(" ", "") + ",";
						}
						content += "|" + sampleValuesStr.substring(0,sampleValuesStr.length()-1);
						break;
					}
					case "date":{
						content += "|" + type;
						for (String line : condition_date_xml) {
							String _line = new String(line);
							_line = _line.replace("${key}", key);
							_line = _line.replace("${name}", name);
							_line = _line.replace("${slot}", slot);
							templateConditionConfig.append(_line);
						}		
						break;
					}
					case "datetime":{
						content += "|" + type;
						for (String line : condition_datetime_xml) {
							String _line = new String(line);
							_line = _line.replace("${key}", key);
							_line = _line.replace("${name}", name);
							_line = _line.replace("${slot}", slot);
							templateConditionConfig.append(_line);
						}		
						break;
					}
					default:
						break;
				}
				sampleOriginList.add(content);
			}
			continue;
		}
		
		//落地hubot-interface配置文件
		String configFileName = "hubot-"+model.replace(".", "-")+".xml";
		String result = "";
		File resultFile = new File(xmlPath + PATH_SEP + configFileName);
		if(!resultFile.exists())resultFile.createNewFile();
		for (String line : config_xml) {
			String _line = new String(line);
			_line = _line.replace("${thing}", thing);
			_line = _line.replace("${intent}", intent);
			_line = _line.replace("${model}", model);
			_line = _line.replace("${conditions}", templateConditionConfig.toString());
			result += _line;
		}
		XmlUtils.parseText(result);//格式化HTML
		FileUtils.writeStringToFile(resultFile, XmlUtils.formatHtml(result));
		
		//落地对话样本文件
		String sampleDefinedFileName = "对话样本定义("+model.replace(".", "-")+").txt";
		File sampleDefinedFile = new File(xmlPath + PATH_SEP + sampleDefinedFileName);
		FileUtils.writeLines(sampleDefinedFile, "UTF-8", sampleOriginList, false);
		
		String sampleFileName = "对话样本("+model.replace(".", "-")+").txt";
		File sampleFile = new File(xmlPath + PATH_SEP + sampleFileName);
		List<String> samples = sampleService.generateSample(sampleDefinedFile);
		FileUtils.writeLines(sampleFile, "UTF-8", samples, false);
		
		//打包
		List<File> zipFiles = ImmutableList.of(resultFile, sampleDefinedFile, sampleFile);
		File zip = new File(xmlPath + PATH_SEP + "config("+model.replace(".", "-") + ").zip"); 
		ZipUtil.zipFile(zipFiles, zip);
		
		//下载
		com.dvt.HubotService.commons.utils.FileUtils.downloadFile(zip, request, response);
	}
	
	private String outputXml(HttpServletRequest request, Map fieldsViews){
		//视图
		Map list = null;
		Map search = null;
		Map form = null;
		if(fieldsViews.containsKey("list")){
			list = (HashMap)fieldsViews.get("list");
		}
		if(fieldsViews.containsKey("search")){
			search = (HashMap)fieldsViews.get("search");
		}
		if(fieldsViews.containsKey("form")){
			form = (HashMap)fieldsViews.get("form");
		}
		String timestamp = "templates." + new Date().getTime();
		String folderPath = request.getSession().getServletContext().getRealPath("")+"template" + PATH_SEP + timestamp;
		File templateFolder = new File(folderPath);
		if(!templateFolder.exists()){
			templateFolder.mkdir();
			if(list!=null)
				XmlUtils.toXMLFile(XmlUtils.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEP + list.get("arch").toString()), folderPath+PATH_SEP+"tree.xml", null);
			if(form!=null)
				XmlUtils.toXMLFile(XmlUtils.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEP + form.get("arch").toString()), folderPath+PATH_SEP+"form.xml", null);
			if(search!=null)
				XmlUtils.toXMLFile(XmlUtils.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEP + search.get("arch").toString()), folderPath+PATH_SEP+"search.xml", null);
		}
		return folderPath;
	}
}
