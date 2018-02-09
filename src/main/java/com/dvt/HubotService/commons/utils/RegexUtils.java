package com.dvt.HubotService.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RegexUtils {
	
	private static final String datePattern = ".*(\\d{4})年(\\d{1,2})月(\\d{1,2})日((\\d{1,2})[时|点])?((\\d{1,2})分)?((\\d{1,2})秒)?.*";
	public static Pattern r = Pattern.compile(datePattern);
	
	public static String compileDate(String msgVar){
		Matcher m = r.matcher(msgVar);
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
	    	System.out.println(msgVar+" 转化为: "+dateResult);
	    	return dateResult;
		}else{
			System.out.println("NO MATCH");
			return null;
		}
	}
}