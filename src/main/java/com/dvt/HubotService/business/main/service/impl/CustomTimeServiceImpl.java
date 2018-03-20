package com.dvt.HubotService.business.main.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dvt.HubotService.business.main.dto.EntryBO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;
import com.dvt.HubotService.business.main.service.CustomTimeService;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.time.nlp.TimeUnit;
import com.time.nlp.stringPreHandlingModule;
@Service
public class CustomTimeServiceImpl implements CustomTimeService{

	@Override
	public String controlTimeVar(HubotInterface myHif, EntryBO ebo) {
		List<EntryBO> timeVars = Lists.newArrayList(myHif.getArgEntryMap().values());
		//找出所需的所有时间变量
		timeVars = timeVars.stream().filter(_ebo -> "date".equals(_ebo.getType()) && _ebo.getIsInUse()).collect(Collectors.toList());
    	
    	if(CollectionUtils.isNotEmpty(timeVars)){
    		if(timeVars.size()==1){
    			String timeOrginStr = (String)timeVars.get(0).getTempValue();
    			GlobalConstants.normalizer.parse(timeOrginStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			String timeNlpTimeStr = CommonHelper.date2Str(unit[0].getTime(), CommonHelper.DF_DATE_TIME);
				return timeNlpTimeStr;
    		}else{//多于一个
    			//time-nlp需要让它们出现在同一句话里
    			String sampleOrignStr = "";
    			for (int i = 0; i < timeVars.size(); i++) {
    				EntryBO _ebo = timeVars.get(i);
    				sampleOrignStr+= (String)_ebo.getTempValue()+",";
    			}
    			sampleOrignStr = sampleOrignStr.substring(0,sampleOrignStr.length()-1);
    			
    			GlobalConstants.normalizer.parse(sampleOrignStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			if(unit.length == unitStr.size()){
    	    		for (EntryBO _ebo : timeVars) {
    	    			for (int i = 0; i < unitStr.size(); i++) {
    	    				String lowercaseTempValue = stringPreHandlingModule.numberTranslator(_ebo.getTempValue());
    	    				if(unitStr.get(i).contains(lowercaseTempValue) || lowercaseTempValue.contains(unitStr.get(i))){
    							String dateFormat = CommonHelper.DF_DATE_TIME;
    							if(StringUtils.isNotBlank(_ebo.getDateFormat())){
    								dateFormat = _ebo.getDateFormat();
    							}
    							String timeNlpTimeStr = CommonHelper.date2Str(unit[i].getTime(), dateFormat);
    							
    							if(StringUtils.equals(_ebo.getEntry().getKey(), ebo.getEntry().getKey())){
    								return timeNlpTimeStr;
    							}
    						}
    					}
    				}
    	    	}
    		}
    	}
    	return "";
	}

	@Override
	public String controlTimeVar(HubotInterface myHif, QueryBO qbo) {
		List<QueryBO> timeVars = Lists.newArrayList(myHif.getQueryConditionMap().values());
		//找出所需的所有时间变量
		timeVars = timeVars.stream().filter(_qbo -> "date".equals(_qbo.getType()) && _qbo.getIsInUse()).collect(Collectors.toList());
    	
    	if(CollectionUtils.isNotEmpty(timeVars)){
    		if(timeVars.size()==1){
    			String timeOrginStr = (String)timeVars.get(0).getTempValue();
    			GlobalConstants.normalizer.parse(timeOrginStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			String timeNlpTimeStr = CommonHelper.date2Str(unit[0].getTime(), CommonHelper.DF_DATE_TIME);
				return timeNlpTimeStr;
    		}else{//多于一个
    			//time-nlp需要让它们出现在同一句话里
    			String sampleOrignStr = "";
    			for (int i = 0; i < timeVars.size(); i++) {
    				QueryBO _qbo = timeVars.get(i);
    				sampleOrignStr+= (String)_qbo.getTempValue()+",";
    			}
    			sampleOrignStr = sampleOrignStr.substring(0,sampleOrignStr.length()-1);
    			
    			GlobalConstants.normalizer.parse(sampleOrignStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			if(unit.length == unitStr.size()){
    	    		for (QueryBO _qbo : timeVars) {
    	    			for (int i = 0; i < unitStr.size(); i++) {
    	    				String lowercaseTempValue = stringPreHandlingModule.numberTranslator(_qbo.getTempValue());
    						if(unitStr.get(i).contains(lowercaseTempValue) || lowercaseTempValue.contains(unitStr.get(i))){
    							String dateFormat = CommonHelper.DF_DATE_TIME;
    							if(StringUtils.isNotBlank(_qbo.getDateFormat())){
    								dateFormat = _qbo.getDateFormat();
    							}
    							String timeNlpTimeStr = CommonHelper.date2Str(unit[i].getTime(), dateFormat);
    							
    							if(StringUtils.equals(_qbo.getSlot(), qbo.getSlot())){
    								return timeNlpTimeStr;
    							}
    						}
    					}
    				}
    	    	}
    		}
    	}
    	return "";
	}

	@Override
	public List<String> controlBoundaryTimeVar(HubotInterface myHif, QueryBO qbo) {
		List<QueryBO> timeVars = Lists.newArrayList(myHif.getQueryConditionMap().values());
		//找出所需的所有时间变量
		timeVars = timeVars.stream().filter(_qbo -> "date".equals(_qbo.getType()) && _qbo.getIsInUse()).collect(Collectors.toList());
    	
    	if(CollectionUtils.isNotEmpty(timeVars)){
    		if(timeVars.size()==1){
    			String timeOrginStr = (String)timeVars.get(0).getTempValue();
    			GlobalConstants.normalizer.parse(timeOrginStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			return ImmutableList.of(CommonHelper.date2Str(unit[0].getTime(), CommonHelper.DF_DATE_TIME),
    							 		CommonHelper.date2Str(unit[0].getBoundaryTime(), CommonHelper.DF_DATE_TIME));
    		}else{//多于一个
    			//time-nlp需要让它们出现在同一句话里
    			String sampleOrignStr = "";
    			for (int i = 0; i < timeVars.size(); i++) {
    				QueryBO _qbo = timeVars.get(i);
    				sampleOrignStr+= (String)_qbo.getTempValue()+",";
    			}
    			sampleOrignStr = sampleOrignStr.substring(0,sampleOrignStr.length()-1);
    			
    			GlobalConstants.normalizer.parse(sampleOrignStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			if(unit.length == unitStr.size()){
    	    		for (QueryBO _qbo : timeVars) {
    	    			for (int i = 0; i < unitStr.size(); i++) {
    	    				String lowercaseTempValue = stringPreHandlingModule.numberTranslator(_qbo.getTempValue());
    						if(unitStr.get(i).contains(lowercaseTempValue) || lowercaseTempValue.contains(unitStr.get(i))){
    							String dateFormat = CommonHelper.DF_DATE_TIME;
    							if(StringUtils.isNotBlank(_qbo.getDateFormat())){
    								dateFormat = _qbo.getDateFormat();
    							}
    							
    							if(StringUtils.equals(_qbo.getSlot(), qbo.getSlot())){
    								return ImmutableList.of(CommonHelper.date2Str(unit[0].getTime(), CommonHelper.DF_DATE_TIME),
        							 		CommonHelper.date2Str(unit[0].getBoundaryTime(), CommonHelper.DF_DATE_TIME));
    							}
    						}
    					}
    				}
    	    	}
    		}
    	}
    	return null;
	}
	
}
