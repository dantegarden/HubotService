package com.dvt.HubotService.business.main.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.dvt.HubotService.business.main.dto.DictDTO;
import com.dvt.HubotService.business.main.dto.DictParam;
import com.dvt.HubotService.business.main.dto.DictTypeDTO;
import com.dvt.HubotService.business.main.service.DictService;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.dvt.HubotService.commons.utils.CommonHelper.TimeUnit;
import com.dvt.HubotService.commons.utils.HttpHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@Service
public class DictServiceImpl implements DictService{
	
	@SuppressWarnings("finally")
	@Override
	public List<DictDTO> getDictByType(String url, String codeType, String modal, String field) {
		List<DictDTO> dicts = Lists.newArrayList();
		if(GlobalConstants.codeDicts.containsKey(codeType)
				&& checkTimeEffect(codeType)){
			return GlobalConstants.codeDicts.get(codeType).getDict();
		}else{
			try {
				String content_type = "application/x-www-form-urlencoded";
				Map<String,String> params = new ImmutableMap.Builder<String,String>()
						.put("params", JsonUtils.JavaBeanToJson(new DictParam(modal, field)))
						.build();
				String backJson = HttpHelper.startPost(url, params, content_type);
				dicts = JsonUtils.jsonToList(backJson, DictDTO.class);//codeType,dict
				GlobalConstants.codeDicts.put(codeType, new DictTypeDTO(dicts, new Date(), codeType));
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				return dicts;
			}
		}
	}
	
	@SuppressWarnings("finally")
	@Override
	public List<DictDTO> getDictByModalAndField(String url, String modal, String field) {
		String codeType = modal+"|"+field;
		List<DictDTO> dicts = Lists.newArrayList();
		if(GlobalConstants.codeDicts.containsKey(codeType)
				&& checkTimeEffect(codeType)){
			return GlobalConstants.codeDicts.get(codeType).getDict();
		}else{
			try {
				String content_type = "application/x-www-form-urlencoded";
				Map<String,String> params = new ImmutableMap.Builder<String,String>()
						.put("params", JsonUtils.JavaBeanToJson(new DictParam(modal, field)))
						.build();
				String backJson = HttpHelper.startPost(url, params, content_type);
				dicts = JsonUtils.jsonToList(backJson, DictDTO.class);//codeType,dict
				GlobalConstants.codeDicts.put(codeType, new DictTypeDTO(dicts, new Date(), codeType));
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				return dicts;
			}
		}
	}
		
	private boolean checkTimeEffect(String codeType){
		boolean isEffect = Boolean.FALSE;
		if(GlobalConstants.codeDicts.containsKey(codeType)){
			DictTypeDTO dtDto = GlobalConstants.codeDicts.get(codeType);
			if(dtDto.getAccessTime()!=null && CollectionUtils.isNotEmpty(dtDto.getDict())){
				long diff = CommonHelper.Timediff(new Date(), dtDto.getAccessTime(), TimeUnit.SECOND);
				if(diff > 0){
					isEffect = Boolean.TRUE;
				}
			}
		}
		return isEffect;
	}

	
}
