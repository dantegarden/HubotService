package com.dvt.HubotService.business.main.slot.global.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dvt.HubotService.business.main.dto.DictDTO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;
import com.dvt.HubotService.business.main.service.DictService;
import com.dvt.HubotService.business.main.slot.global.UserDefinedSlotForCondition;
import com.dvt.HubotService.commons.utils.HanyuPinyinHelper;
import com.google.common.collect.Lists;
@Component
public class CustomSelectionSlotCondition implements UserDefinedSlotForCondition {
	
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
				persons = dictService.getDictByType(myHif.getDictUrl(),codeType,model,field);
			}else{
				persons = dictService.getDictByModalAndField(myHif.getDictUrl(),model,field);
			}
			
			for (DictDTO dictDTO : persons) {
				if(HanyuPinyinHelper.comparePinyin(dictDTO.getDictKey(), qbo.getTempValue())){
					myCondition.add(qbo.getKey());
					myCondition.add("=");
					myCondition.add(dictDTO.getDictValue());//id是value， 汉字名字是key
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return myCondition;
	}

}
