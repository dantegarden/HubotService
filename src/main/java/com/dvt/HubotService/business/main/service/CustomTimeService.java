package com.dvt.HubotService.business.main.service;

import java.util.List;

import com.dvt.HubotService.business.main.dto.EntryBO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;

public interface CustomTimeService {
	/**
	 * 针对键值对类型配置
	 * 处理时间类型的默认方法
	 * **/
	public String controlTimeVar(HubotInterface myHif, EntryBO ebo);
	/**
	 * 针对数组类型配置
	 * 处理时间类型的默认方法
	 * **/
	public String controlTimeVar(HubotInterface myHif, QueryBO qbo);
	/**
	 * 从一个时间中解析出时间段的上下边界
	 * */
	public List<String> controlBoundaryTimeVar(HubotInterface myHif, QueryBO qbo);
}
