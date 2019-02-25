package com.dvt.HubotService.commons.test;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;




import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.dvt.HubotService.business.main.dto.DictDTO;
import com.dvt.HubotService.business.main.service.SampleService;
import com.dvt.HubotService.business.main.service.impl.SampleServiceImpl;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.dvt.HubotService.commons.utils.HanyuPinyinHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.dvt.HubotService.commons.utils.XmlRpcUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.time.util.DateUtil;


public class TestMethod {
	
	Map<String,Object> map = Maps.newHashMap();
	Map<String,Object> map2 = Maps.newHashMap();
	
	//@Test
	public void test2(){
		ExpressionParser parser =new SpelExpressionParser();  
		StandardEvaluationContext context =new StandardEvaluationContext();  
        context.setVariable("i", 3);  
        context.setVariable("j", 2);  
        Integer value =parser.parseExpression("#i - #j").getValue(context, Integer.class);  
        System.out.println(value);
        //assertThat(value ,is("Hello World"));  
	}
	
	private String checkTime(String timeField){
		if(timeField.length()==1){
			timeField = "0" + timeField; 
		}
		return timeField;
	}
	
	//@Test
	public void test(){
		String line = "结束日期是2018年1月19日14点9分30秒";
	    //String pattern = "(\\D*)(\\d+)(.*)";
		String pattern = ".*(\\d{4})年(\\d{1,2})月(\\d{1,2})日((\\d{1,2})[时|点])?((\\d{1,2})分)?((\\d{1,2})秒)?.*";
		 
	      // 创建 Pattern 对象
	    Pattern r = Pattern.compile(pattern);
	 
	      // 现在创建 matcher 对象
	    Matcher m = r.matcher(line);
	    //System.out.println(m.matches());
	    if (m.find()) {
	    	String year=""+CommonHelper.getNow().getYear();
	    	String month="01";
	    	String day="01";
	    	String hour="00";
	    	String minute="00";
	    	String second="00";
	    	
	    	for (int i = 0; i < m.groupCount()+1; i++) {
	    		String timeField = m.group(i);
	    		System.out.println("Found value"+i+": " + timeField);
	    		if(StringUtils.isNotBlank(timeField)){
	    			switch (i) {
					case 1:{year = timeField; break;}
					case 2:{month = CommonHelper.checkTime(timeField); break;}
					case 3:{day = CommonHelper.checkTime(timeField); break;}
					case 5:{hour = CommonHelper.checkTime(timeField); break;}
					case 7:{minute = CommonHelper.checkTime(timeField); break;}
					case 9:{second = CommonHelper.checkTime(timeField); break;}
					default:
						break;
					}
	    		}
			}
	    	
	    	String dateResult = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
	    	System.out.println(dateResult);
	    	
	    } else {
	         System.out.println("NO MATCH");
	    }
		
	}
	
//	@Test
//	public void cxf() throws Exception{
//		try {
//			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//	        org.apache.cxf.endpoint.Client client = dcf
//	                .createClient("http://localhost:8080/UESTCService/cxf/server?wsdl");
//	        // url为调用webService的wsdl地址
//	        QName name = new QName("http://impl.webservice.example.real.business.uestcservice.dvt.com/", "modWorkflowUser");
//	        // namespace是命名空间，methodName是方法名
//	        String xmlStr = "[{'userid': 31222, 'name': u'\u4e01\u5e86', 'roles': [{'roleid': u'XSCGCJSP_XGBSP', 'name': u'\u5b66\u5de5\u90e8\u5ba1\u6279'}, {'roleid': u'XSCGCJSP_XYDWSP_0101', 'name': u'\u5b66\u9662\u515a\u59d4\u5ba1\u6279'}]}]";
//	        // paramvalue为参数值
//	        Object[] objects;
//        
//            objects = client.invoke(name, xmlStr);
//            System.out.println(objects[0].toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//	}
	//@Test
	public void test22(){
		List<DictDTO> sampleValues = Lists.newArrayList();
		sampleValues.add(new DictDTO("111","a"));
		sampleValues.add(new DictDTO("111","b"));
		sampleValues.add(new DictDTO("222","c"));
		sampleValues = sampleValues.stream().collect(Collectors.collectingAndThen(
				Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DictDTO::getDictKey))), ArrayList::new));
		for (DictDTO dictDTO : sampleValues) {
			System.out.println(dictDTO.getDictKey());
		}
	}
//	@Test
//	public void test23(){
//		int uid = 1;
//		String db = "dris_erp";
//		String uname = "driserp";
//		String pwd = "123456";
//		String model = "sale.order";
//		String odooUrl = "http://139.129.225.173:7060";
//		String url = String.format("%s/xmlrpc/2/object", odooUrl);
//		int action_id = 380;
//		
//		Map context = ImmutableMap.of(
//				"lang", "zh_CN",
//				"tz", "Asia/Hong_Kong",
//				"uid", uid,
//				"show_sale", true,
//				"params", ImmutableMap.of("action",action_id)
//		);
//		List domain = asList(asList("order_name","ilike","设备"));
//		
//		Object o = XmlRpcUtils.getXMLRPC(url, "execute_kw", asList(db, uid, pwd, model,"search_read",new ArrayList(),
//		         new ImmutableMap.Builder<String, Object>()
//				 .put("context", context)
//				 .put("domain", domain)
//				 .put("fields", asList("order_name","state"))
//				 .put("limit", 80).build()
//		));
//		List r =asList((Object[])o); 
//		for (Object object : r) {
//			Map<String,Object> a = (HashMap)object;
//			Set<String>akeySet = a.keySet();
//			for (String key : akeySet) {
//				System.out.println(key +"-->" + a.get(key));
//			}
//		}
//		
//		System.out.println("1");
//	}
	
//	private List asList(Object...a){
//		return Arrays.asList(a);
//	}
	
	@Test
	public void testSample() throws Exception{
		SampleServiceImpl sampleService = new SampleServiceImpl();
		String filepath = "E:\\chrome\\对话样本定义(sale-order).txt";
		String targetpath = "E:\\chrome\\对话样本(sale-order).txt";
		File sampleDefinedFile = new File(filepath);
		File targetFile = new File(targetpath);
		List<String> samples = sampleService.generateSampleV2(sampleDefinedFile);
		FileUtils.writeLines(targetFile, "UTF-8", samples, false);;
	}
}
