package com.dvt.HubotService.business.main.service;

import java.util.List;

import com.dvt.HubotService.business.main.dto.DictDTO;

public interface DictService {
	/**取odoo中selection类型的字典
	 * 以modal|field为key
	 * **/
	public List<DictDTO> getDictByModalAndField(String url, String modal, String field);
	/**
	 * 取odoo中many2one类型的字典
	 * 以被关联的模型名为key
	 * **/
	public List<DictDTO> getDictByType(String url, String codeType, String modal, String field); 
}
