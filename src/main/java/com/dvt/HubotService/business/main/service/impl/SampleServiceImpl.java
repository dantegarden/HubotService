package com.dvt.HubotService.business.main.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.dvt.HubotService.business.main.service.SampleService;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.time.util.DateUtil;
@Service
public class SampleServiceImpl implements SampleService{
	
	private Calendar calendar = Calendar.getInstance();
	private Calendar calendar2 = Calendar.getInstance();
	private List<String> dateSampleSeq = null;
	private List<String> datetimeSampleSeq = null;
	private List<String[]> dateSampleSeq2 = null;
	private List<String[]> datetimeSampleSeq2 = null;
	
	public SampleServiceImpl() {
		super();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -1);
		calendar.add(Calendar.MONTH, -6);
		calendar.add(Calendar.DAY_OF_MONTH, -15);
		calendar.add(Calendar.HOUR_OF_DAY, -12);
		calendar.add(Calendar.MINUTE, -30);
		
		calendar2.setTime(new Date());
		
		dateSampleSeq = ImmutableList.of(
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
		
		datetimeSampleSeq = ImmutableList.of(
				 CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点"),
				 CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H点m分"),
				 CommonHelper.date2Str(calendar.getTime(), "yyyy年M月d日H:m"),
				 CommonHelper.date2Str(calendar2.getTime(), "yyyy年M月d日H:m"),
				 CommonHelper.date2Str(calendar.getTime(), "M月d日H:m"),
				 CommonHelper.date2Str(calendar2.getTime(), "M月d日H:m"),
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
				 DateUtil.dateToUpper(new Date(), "dd日HH点mm"),
				 DateUtil.dateToUpper(new Date(), "HH点"),
				 DateUtil.dateToUpper(new Date(), "HH点mm分"));
		
		dateSampleSeq2 = ImmutableList.of(
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
		
		datetimeSampleSeq2 = ImmutableList.of(
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
	}

	@Override
	public List<String> generateSample(File sampleDefinedFile) throws IOException {
		// TODO Auto-generated method stub
		List<String> samples = Lists.newArrayList();
		
        File file = sampleDefinedFile;
        
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        String thing = lines.get(0);
        String intent = lines.get(1);
        lines = lines.subList(2, lines.size());
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
						samples.add(sampleLine);
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
						samples.add(sampleLine);
					}
					break;
				}
				case "date":{
					
					for (int i = 0; i < dateSampleSeq.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq.get(i), thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + dateSampleSeq.get(i)
										  + "\n";
						samples.add(sampleLine);
					}
					for (int i = 0; i < dateSampleSeq2.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq2.get(i)[0]+"到"+dateSampleSeq2.get(i)[1], thing) + "\t" 
			                      + intent + "\t"
			                      + slot + ":" + dateSampleSeq2.get(i)[0] + "\t"
			                      + slot+"_e" + ":" + dateSampleSeq2.get(i)[1] + "\t"
								  + "\n";
						samples.add(sampleLine);
					}
					break;
				}
				case "datetime":{
					
					for (int i = 0; i < dateSampleSeq.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq.get(i), thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + dateSampleSeq.get(i)
										  + "\n";
						samples.add(sampleLine);
					}
					for (int i = 0; i < dateSampleSeq2.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, dateSampleSeq2.get(i)[0]+"到"+dateSampleSeq2.get(i)[1], thing) + "\t" 
			                      + intent + "\t"
			                      + slot + ":" + dateSampleSeq2.get(i)[0] + "\t"
			                      + slot+"_e" + ":" + dateSampleSeq2.get(i)[1] + "\t"
								  + "\n";
						samples.add(sampleLine);
					}
					for (int i = 0; i < datetimeSampleSeq.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, datetimeSampleSeq.get(i), thing) + "\t" 
					                      + intent + "\t"
					                      + slot + ":" + datetimeSampleSeq.get(i)
										  + "\n";
						samples.add(sampleLine);
					}
					for (int i = 0; i < datetimeSampleSeq2.size(); i++) {
						String sampleLine = String.format("查询%s是%s的%s", name, datetimeSampleSeq2.get(i)[0]+"到"+datetimeSampleSeq2.get(i)[1], thing) + "\t" 
			                      + intent + "\t"
			                      + slot + ":" + datetimeSampleSeq2.get(i)[0] + "\t"
			                      + slot+"_e" + ":" + datetimeSampleSeq2.get(i)[1] + "\t"
								  + "\n";
						samples.add(sampleLine);
					}
					break;
				}
			default:
				break;
			}
		}
        return samples;
	}
	
}
