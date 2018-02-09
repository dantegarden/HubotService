package com.dvt.HubotService.business.main.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class HubotInterface {
	private String description;//描述
	private String answer;//请求处理后的回复标题
	private PostObject postObject;//发送参数
	private List postObjectList;//发送参数
	private String entryUrl;//请求接口的入口
	
	private Map<String,EntryBO> argEntryMap = Maps.newHashMap();
	
	public Map<String,EntryBO> getArgEntryMap(){
		return argEntryMap;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getEntryUrl() {
		return entryUrl;
	}
	public void setEntryUrl(String entryUrl) {
		this.entryUrl = entryUrl;
	}
	
	public List getPostObjectList() {
		return postObjectList;
	}
	public void setPostObjectList(List postObjectList) {
		this.postObjectList = postObjectList;
	}
	public PostObject getPostObject() {
		return postObject;
	}
	public void setPostObject(PostObject postObject) {
		this.postObject = postObject;
	}
	public HubotInterface() {
		super();
	}
	
	public PostObject arrangeParamsObject(Element object){
		PostObject po = new PostObject();
		
		if(isTag(object, "object")){
			List<Element> entrys = object.elements();
			for (Element entry : entrys) {
					
				String key = entry.attributeValue("key");
				String type = entry.attributeValue("type");
				String slot = entry.attributeValue("slot");//slot-key 百度ai的词槽 与 被调系统所需参数的对应关系
				String comment = entry.attributeValue("comment");
				Boolean required = entry.attributeValue("required")==null?
										Boolean.FALSE:
										Boolean.valueOf(entry.attributeValue("required"));
				
				String defaultValue = entry.getTextTrim();
				
				String dateFormat = entry.attributeValue("date-format");
				
				if(type.equals("list")){//值是数组的键值对
					po.put(key, arrangeParamsArray((Element)entry.elements().get(0)));
				}else if(type.equals("object")){//值是对象的键值对
					po.put(key, arrangeParamsObject((Element)entry.elements().get(0)));
				}else{
					//普通的键值对
					po.put(key, checkType(type, defaultValue));
					//记录需要匹配参数的键值对
					if(StringUtils.isNotBlank(slot)){
						EntryBO ebo =  new EntryBO(key, type, po.getEntryFromMap(key));
						if(StringUtils.isNotBlank(dateFormat))
							ebo.setDateFormat(dateFormat);
						if(StringUtils.isNotBlank(comment))
							ebo.setComment(comment);
						if(required!=null)
							ebo.setRequired(required);
						argEntryMap.put(slot, ebo);
					}
				}
			}
		}
		return po;
	}
	
	public List arrangeParamsArray(Element params){
		List l = Lists.newArrayList();
		
		if(isTag(params, "list")){
			List<Element> list = params.elements();
			for (Element member : list) {
				String tag = member.getName();//标签 可能是 object,list,var	
				
				if(tag.equals("list")){
					l.add(arrangeParamsArray(member));
				}else if(tag.equals("object")){
					l.add(arrangeParamsObject(member));
				}else if(tag.equals("var")){//常量
					l.add(member.getTextTrim());
				}
			}
		}
		return l;
	}
	
	
	
	public static boolean isTag(Element e, String tag){
		String _tag = e.getName();
		return _tag.equals(tag);
	}
	
	public static boolean hasElements(Element e){
		if(CollectionUtils.isEmpty(e.elements())){
			return Boolean.FALSE;
		}else{
			return Boolean.TRUE;
		}
	}
	
	/***规范值的类型**/
	public static Object checkType(String type,String value){
		Object obj = null;
		if(type!=null)
		switch (type) {
			case "string":{
				obj = String.valueOf(value);
				break;
			}
			case "date":{
				obj = String.valueOf(value);
				break;
			}
			case "boolean":{
				obj = Boolean.valueOf(value);
				break;
			}
			case "num":{
				obj = Float.valueOf(value);
				break;
			}
			default:
				break;
		}
		return obj;
	}

	
}
