package com.dvt.HubotService.commons.test;



import java.io.IOException;




import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.dvt.HubotService.commons.utils.CommonHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class TestMethod {
	
	Map<String,Object> map = Maps.newHashMap();
	Map<String,Object> map2 = Maps.newHashMap();
	
	@Test
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
}
