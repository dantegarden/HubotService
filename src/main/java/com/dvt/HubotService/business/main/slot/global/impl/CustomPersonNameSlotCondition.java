package com.dvt.HubotService.business.main.slot.global.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dvt.HubotService.business.main.dto.DictDTO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;
import com.dvt.HubotService.business.main.service.DictService;
import com.dvt.HubotService.business.main.slot.global.UserDefinedSlotForCondition;
import com.dvt.HubotService.business.main.utils.RegexRelationUtils;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.utils.HanyuPinyinHelper;
import com.dvt.HubotService.commons.utils.HttpHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
@Component
public class CustomPersonNameSlotCondition implements UserDefinedSlotForCondition{
	@Autowired
	private DictService dictService;
	
	@Override
	public List<String> dispose(HubotInterface myHif, QueryBO qbo) {
		List<String> myCondition = Lists.newArrayList();
		try {
			String model = (String)myHif.getPostObject().get("model");
			String field = qbo.getKey();
			String codeType = StringUtils.isNotBlank(qbo.getCodeType())?
								qbo.getCodeType():(model+"|"+field);
								
			List<DictDTO> persons = null;
			if(StringUtils.isNotBlank(qbo.getCodeType())){
				persons = dictService.getDictByType(myHif.getDictUrl(), codeType, model, field);
			}else{
				persons = dictService.getDictByModalAndField(myHif.getDictUrl(),model,field);
			}
			
			boolean bdflag = false;
			for (DictDTO dictDTO : persons) {
				if(HanyuPinyinHelper.comparePinyin(dictDTO.getDictKey(), qbo.getTempValue())){
					myCondition.add(qbo.getKey());
					myCondition.add("like");
					myCondition.add(dictDTO.getDictKey());//id是value， 汉字名字是key
					
					bdflag = true;
					break;
				}
			}
			
			if(!bdflag){
				myCondition.add(qbo.getKey());
				myCondition.add("like");
				myCondition.add(qbo.getTempValue());//如果不需要纠正，就返回原值
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return myCondition;
	}
	
}
