package com.dvt.HubotService.business.main.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class RegexRelationUtils {
	
	private static String greaterEqualPattern = "(大于等于|不小于|不低于|不少于|不早于)";
	private static String lessEqualPattern = "(小于等于|不大于|不高于|不超过|不多于|不晚于)";
	private static String greaterPattern = "(大于|超过|之后|以后|高于|多于)";
	private static String lessPattern = "(小于|少于|之前|以前|低于|不到|不足|不满)";
	private static String notEqualPattern = "(不等于|不是)";
	private static String equalPartten = "(等于)";
	private static String notContainsPartten = "(不包含)";
	private static String containsPartten = "(包含)";

	private static Map<String,String> parttenMap =  Maps.newLinkedHashMap();
	static {
		parttenMap.put(greaterEqualPattern, ">=");
		parttenMap.put(lessEqualPattern, "<=");
		parttenMap.put(greaterPattern, ">");
		parttenMap.put(lessPattern, "<");
		parttenMap.put(notEqualPattern, "!=");
		parttenMap.put(equalPartten, "=");
		parttenMap.put(notContainsPartten, "not like");
		parttenMap.put(containsPartten, "like");
	}
	
	
	public static String getRelationExpression(String mes){
		String relationExpression = null;
		Iterator<String> iterator = parttenMap.keySet().iterator();
		while (iterator.hasNext()) {
			String parttenStr =  iterator.next();
			Pattern pattern = Pattern.compile(parttenStr);
			Matcher mr = pattern.matcher(mes);
			while(mr.find()){
				System.out.println(mr.group());
				relationExpression = parttenMap.get(parttenStr);
				break;
		    }
		}
		return relationExpression;
	}
}
