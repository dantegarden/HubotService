package com.dvt.HubotService.business.main.slot.global.impl;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;
import com.dvt.HubotService.business.main.service.CustomTimeService;
import com.dvt.HubotService.business.main.slot.global.UserDefinedSlotForCondition;
import com.dvt.HubotService.business.main.utils.RegexRelationUtils;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.google.common.collect.Lists;
import com.time.nlp.TimeUnit;
import com.time.nlp.stringPreHandlingModule;
@Component
public class CustomStartTimeSlotCondition implements UserDefinedSlotForCondition{
	
	@Autowired
	private CustomTimeService customTimeService;
	
	@Override
	public List<String> dispose(HubotInterface myHif, QueryBO qbo) {
		List<String> myCondition = Lists.newArrayList();
		
		List<QueryBO> timeVars = Lists.newArrayList(myHif.getQueryConditionMap().values());
		
		List<String> timeNlpTimeStrs = customTimeService.controlBoundaryTimeVar(myHif, qbo);
		String timeNlpTimeStr = timeNlpTimeStr = timeNlpTimeStrs.get(0);
		String dateformat = qbo.getDateFormat();
		if(StringUtils.isNotBlank(dateformat)){
			timeNlpTimeStr = CommonHelper.formatDateStr(timeNlpTimeStr, dateformat);
		}
		
		String relationship = RegexRelationUtils.getRelationExpression(qbo.getTempValue());
		if(StringUtils.isBlank(relationship)){
			relationship = ">=";
			//找出所需的所有时间变量
			timeVars = timeVars.stream().filter(_qbo -> "date".equals(_qbo.getType())//时间字段
													&& StringUtils.isBlank(_qbo.getTempValue())//没有临时值
													&& StringUtils.isNotBlank(_qbo.getDateRef())//有关联且关联了这个词槽
													&& qbo.getSlot().equals(_qbo.getDateRef())).collect(Collectors.toList());
			
			if(CollectionUtils.isNotEmpty(timeVars) 
						&& timeVars.size()==1){
				String timeNlpTimeStrs2 = timeNlpTimeStrs.get(1);

				QueryBO lowBoundaryBo = timeVars.get(0);//唯一的关联qbo
				lowBoundaryBo.getCondition().add(lowBoundaryBo.getKey());
				lowBoundaryBo.getCondition().add("<=");
				lowBoundaryBo.getCondition().add(timeNlpTimeStrs2);
				
				String dateformat2 = lowBoundaryBo.getDateFormat();
				if(StringUtils.isNotBlank(dateformat2)){
					timeNlpTimeStrs2 = CommonHelper.formatDateStr(timeNlpTimeStrs2, dateformat2);
					if(StringUtils.equals(timeNlpTimeStr, timeNlpTimeStrs2)){
						relationship = "=";
						lowBoundaryBo.getCondition().clear();//yyyy-MM-dd上下边界一样，只用一个=的条件
					}
				}
				
			}
		}
		
		myCondition.add(qbo.getKey());
		myCondition.add(relationship);
		myCondition.add(timeNlpTimeStr);
		
		return myCondition;
	}
	
}
