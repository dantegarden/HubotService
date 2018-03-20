package com.dvt.HubotService.commons.test;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;




import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.dvt.HubotService.commons.utils.CommonHelper;
import com.dvt.HubotService.commons.utils.HanyuPinyinHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.google.common.collect.ImmutableList;
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
	
	@Test
	public void testSample() throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -1);
		calendar.add(Calendar.MONTH, -6);
		calendar.add(Calendar.DAY_OF_MONTH, -15);
		calendar.add(Calendar.HOUR_OF_DAY, -12);
		calendar.add(Calendar.MINUTE, -30);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		//calendar.add(Calendar.DAY_OF_YEAR, -10);
		List<String> dateSampleSeq = ImmutableList.of(
				"今天","昨天","前天","后天","明天","大前天","大后天","上周","下周","这周","本周","上上周","下下周",
				"上星期","下星期","这星期","本星期","上上星期","下下星期","本月","这月","这个月","上月","上个月","下个月","下月",
				"今年","去年","前年","后年","明年","周一","上周一","下周一","本周一","这周一","星期二","上星期二","这星期二","本星期二","下星期二",
				CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日"),
				CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d号"),
				CommonHelper.date2Str(calendar.getTime(), "yyyy年M月"),
				CommonHelper.date2Str(calendar.getTime(), "yyyy年"),
				CommonHelper.date2Str(calendar.getTime(), "M月d日"),
				CommonHelper.date2Str(calendar.getTime(), "M月"),
				CommonHelper.date2Str(calendar.getTime(), "d日"),
				DateUtil.dateToUpper(new Date(), "yyyy年MM月dd日"),//数字中文大写
				DateUtil.dateToUpper(new Date(), "MM月dd日"),
				DateUtil.dateToUpper(new Date(), "yyyy年MM月"),
				DateUtil.dateToUpper(new Date(), "dd日"),
				DateUtil.dateToUpper(new Date(), "MM月"),
				DateUtil.dateToUpper(new Date(), "yyyy年")
		); 
		
		List<String> datetimeSampleSeq = ImmutableList.of(
			 CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点"),
			 CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点m分"),
			 CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H:m"),
			 CommonHelper.date2Str(calendar.getTime(), "M月d日H:m"),
			 CommonHelper.date2Str(calendar.getTime(), "M月d日H点"),
			 CommonHelper.date2Str(calendar.getTime(), "M月d日H点m"),
			 CommonHelper.date2Str(calendar.getTime(), "M月d日H点m分"),
			 CommonHelper.date2Str(calendar.getTime(), "d日H点"),
			 CommonHelper.date2Str(calendar.getTime(), "d日H点m"),
			 CommonHelper.date2Str(calendar.getTime(), "d日H点m分"),
			 CommonHelper.date2Str(calendar.getTime(), "H点"),
			 CommonHelper.date2Str(calendar.getTime(), "H点m"),
			 CommonHelper.date2Str(calendar.getTime(), "H点m分"),
			 DateUtil.dateToUpper(new Date(), "yyyy年MM月dd日HH点"),//数字中文大写
			 DateUtil.dateToUpper(new Date(), "MM月dd日HH点mm分"),
			 DateUtil.dateToUpper(new Date(), "MM月dd日HH点"),
			 DateUtil.dateToUpper(new Date(), "dd日HH点"),
			 DateUtil.dateToUpper(new Date(), "dd日HH点mm分"),
			 DateUtil.dateToUpper(new Date(), "HH点"),
			 DateUtil.dateToUpper(new Date(), "HH点mm分"));
		
		List<String[]> dateSampleSeq2 = ImmutableList.of(
				new String[]{"昨天","今天"},
				new String[]{"前天","昨天"},
				new String[]{"今天","明天"},
				new String[]{"明天","后天"},
				new String[]{"后天","大后天"},
				new String[]{"上周","这周"},
				new String[]{"上星期","这星期"},
				new String[]{"上月","这月"},
				new String[]{"本月","下月"},
				new String[]{"本周一","本周二"},
				new String[]{"周一","周五"},
				new String[]{"星期一","星期三"},
				new String[]{"下周一","下周三"},
				new String[]{"这星期","下星期"},
				new String[]{"去年","今年"},
				new String[]{"今年","明年"},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日"),
							CommonHelper.date2Str(calendar2.getTime(), "yyyy年M月d日")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日"),
							 CommonHelper.date2Str(calendar2.getTime(), "M月d日")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日"),
						 	 CommonHelper.date2Str(calendar2.getTime(), "d日")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "M月d日"),
							 CommonHelper.date2Str(calendar2.getTime(), "M月d日")},
                new String[]{CommonHelper.date2Str(calendar.getTime(), "M月d日"),
						     CommonHelper.date2Str(calendar2.getTime(), "d日")},
			    new String[]{CommonHelper.date2Str(calendar.getTime(), "d日"),
					 	     CommonHelper.date2Str(calendar2.getTime(), "d日")}
		); 
		List<String[]> datetimeSampleSeq2 = ImmutableList.of(
				new String[]{CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点"),
							CommonHelper.date2Str(calendar2.getTime(), "yyyy年M月d日H点")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点"),
							 CommonHelper.date2Str(calendar2.getTime(), "yyyy年M月d日H点m分")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点m分"),
						 	 CommonHelper.date2Str(calendar2.getTime(), "yyyy年M月d日H点m分")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点m"),
							 CommonHelper.date2Str(calendar2.getTime(), "yyyy年M月d日H点m")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "M月d日"),
							 CommonHelper.date2Str(calendar2.getTime(), "M月d日H点")},
                new String[]{CommonHelper.date2Str(calendar.getTime(), "M月d日H点"),
						     CommonHelper.date2Str(calendar2.getTime(), "M月d日H点")},
			    new String[]{CommonHelper.date2Str(calendar.getTime(), "H点"),
					 	     CommonHelper.date2Str(calendar2.getTime(), "H点")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "M月d日H点"),
							 CommonHelper.date2Str(calendar2.getTime(), "H点")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "H点m分"),
					     	CommonHelper.date2Str(calendar2.getTime(), "H点m分")},
				new String[]{CommonHelper.date2Str(calendar.getTime(), "H点m"),
			     			CommonHelper.date2Str(calendar2.getTime(), "H点m")}
		);
		
		String readName = "D:\\样本.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
		File writtenFile =  new File("D:\\自动样本.txt");  
        File file = new File(readName);
        writtenFile.delete();
        if(!writtenFile.exists()){
        	writtenFile.createNewFile();
        }  
        
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        String thing = lines.get(0);
        String intent = lines.get(1);
        lines = lines.subList(2, lines.size()-1);
        for (String line : lines) {
			String[] lineArray = line.split("\\|");
			
			String slot = lineArray[0];
			String name = lineArray[1];
			String type = lineArray[2];
			String example = lineArray.length==4?lineArray[3]:"";
			
			switch (type) {
				case "string":{
					String[] e = example.split(",");
					for (int i = 0; i < e.length; i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, e[i], thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + e[i]
					                      + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine, true); 
					}
					break;
				}
				case "selection":{
					String[] e = example.split(",");
					for (int i = 0; i < e.length; i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, e[i], thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + e[i]
										  + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine,"UTF-8", true);
					}
					break;
				}
				case "date":{
					
					for (int i = 0; i < dateSampleSeq.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq.get(i), thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + dateSampleSeq.get(i)
										  + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine,"UTF-8", true);
					}
					for (int i = 0; i < dateSampleSeq2.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq2.get(i)[0]+"到"+dateSampleSeq2.get(i)[1], thing) + "\t" 
			                      + intent + "\t"
			                      + slot + ":" + dateSampleSeq2.get(i)[0] + "\t"
			                      + slot+"_e" + ":" + dateSampleSeq2.get(i)[1] + "\t"
								  + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine,"UTF-8", true);
					}
					break;
				}
				case "datetime":{
					
					for (int i = 0; i < dateSampleSeq.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq.get(i), thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + dateSampleSeq.get(i)
										  + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine,"UTF-8", true);
					}
					for (int i = 0; i < dateSampleSeq2.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq2.get(i)[0]+"到"+dateSampleSeq2.get(i)[1], thing) + "\t" 
			                      + intent + "\t"
			                      + slot + ":" + dateSampleSeq2.get(i)[0] + "\t"
			                      + slot+"_e" + ":" + dateSampleSeq2.get(i)[1] + "\t"
								  + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine,"UTF-8", true);
					}
					for (int i = 0; i < datetimeSampleSeq.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, datetimeSampleSeq.get(i), thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + datetimeSampleSeq.get(i)
										  + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine,"UTF-8", true);
					}
					for (int i = 0; i < datetimeSampleSeq2.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, datetimeSampleSeq2.get(i)[0]+"到"+datetimeSampleSeq2.get(i)[1], thing) + "\t" 
			                      + intent + "\t"
			                      + slot + ":" + datetimeSampleSeq2.get(i)[0] + "\t"
			                      + slot+"_e" + ":" + datetimeSampleSeq2.get(i)[1] + "\t"
								  + "\n";
						FileUtils.writeStringToFile(writtenFile, sampleLine,"UTF-8", true);
					}
					break;
				}
			default:
				break;
			}
		}
        
	}
}
