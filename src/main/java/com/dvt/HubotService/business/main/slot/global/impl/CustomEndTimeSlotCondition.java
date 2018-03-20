package com.dvt.HubotService.business.main.slot.global.impl;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;
import com.dvt.HubotService.business.main.service.CustomTimeService;
import com.dvt.HubotService.business.main.slot.global.UserDefinedSlotForCondition;
import com.dvt.HubotService.business.main.utils.RegexRelationUtils;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.google.common.collect.Lists;
@Component
public class CustomEndTimeSlotCondition implements UserDefinedSlotForCondition{
	
	@Autowired
	private CustomTimeService customTimeService;
	
	@Override	
	public List<String> dispose(HubotInterface myHif, QueryBO qbo) {
		List<String> myCondition = Lists.newArrayList();
		
		List<QueryBO> timeVars = Lists.newArrayList(myHif.getQueryConditionMap().values());
		//找出所需的所有时间变量
		timeVars = timeVars.stream().filter(_qbo -> "date".equals(_qbo.getType()) && _qbo.getIsInUse()).collect(Collectors.toList());
		String timeNlpTimeStr = customTimeService.controlTimeVar(myHif, qbo);
		
		if(timeNlpTimeStr.endsWith("00:00:00")){
			Calendar cal = Calendar.getInstance();
			cal.setTime(CommonHelper.str2Date(timeNlpTimeStr, CommonHelper.DF_DATE_TIME));
			cal.add(Calendar.SECOND, (23 * 60 * 60 + 59 * 60 + 59));
			timeNlpTimeStr = CommonHelper.date2Str(cal.getTime(), CommonHelper.DF_DATE_TIME);
		}
		
		String relationship = RegexRelationUtils.getRelationExpression(qbo.getTempValue());
		if(StringUtils.isBlank(relationship)){
			relationship = "<=";
		}
		
		myCondition.add(qbo.getKey());
		myCondition.add(relationship);
		myCondition.add(timeNlpTimeStr);
		
		return myCondition;
	}

}
