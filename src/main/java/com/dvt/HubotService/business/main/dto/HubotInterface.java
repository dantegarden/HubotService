package com.dvt.HubotService.business.main.dto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;

public class HubotInterface  implements Cloneable, Serializable {
	private String description;//描述
	private String answer;//请求处理后的回复标题
	private PostObject postObject;//发送参数
	private List postObjectList;//发送参数
	private String entryUrl;//请求接口的入口
	private String dictUrl;//字典接口的入口
	//不转换为结果json的属性
	@JsonIgnore
	private Map<String,EntryBO> argEntryMap = Maps.newHashMap();
	@JsonIgnore
	private Map<String,QueryBO> queryConditionMap = Maps.newHashMap();
	
	public Map<String,EntryBO> getArgEntryMap(){
		return argEntryMap;
	}
	public Map<String, QueryBO> getQueryConditionMap() {
		return queryConditionMap;
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
	public String getDictUrl() {
		return dictUrl;
	}
	public void setDictUrl(String dictUrl) {
		this.dictUrl = dictUrl;
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
				String dateRef = entry.attributeValue("date-ref");
				String codeType = entry.attributeValue("code-type");
				
				String useClass = entry.attributeValue("class");
				String useMethod = entry.attributeValue("method");
				
				
				if(type.equals("list")){//值是数组的键值对
					po.put(key, arrangeParamsArray((Element)entry.elements().get(0)));
				}else if(type.equals("object")){//值是对象的键值对
					po.put(key, arrangeParamsObject((Element)entry.elements().get(0)));
				}else{
					//普通的键值对
					po.put(key, checkType(type, defaultValue));
					//记录需要匹配参数的键值对
					if(StringUtils.isNotBlank(slot)){
						EntryBO ebo =  new EntryBO(key, slot, type, po.getEntryFromMap(key));
						if(StringUtils.isNotBlank(dateFormat))
							ebo.setDateFormat(dateFormat);
						if(StringUtils.isNotBlank(dateRef))
							ebo.setDateRef(dateRef);
						if(StringUtils.isNotBlank(comment))
							ebo.setComment(comment);
						if(StringUtils.isNotBlank(codeType))
							ebo.setCodeType(codeType);
						if(required!=null)
							ebo.setRequired(required);
						if(StringUtils.isNotBlank(defaultValue))
							ebo.setIsInUse(Boolean.TRUE);
						if(StringUtils.isNotBlank(useClass))
							ebo.setUseClass(useClass);
						if(StringUtils.isNotBlank(useMethod))
							ebo.setUseMethod(useMethod);
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
				String tag = member.getName();//标签 可能是 object,list,condition,value	
				
				if(tag.equals("list")){
					l.add(arrangeParamsArray(member));
				}else if(tag.equals("object")){
					l.add(arrangeParamsObject(member));
				}else if(tag.equals("condition")){//查询条件 格式为[key,connection,value]
					
					String key = member.attributeValue("key");//查询条件的第一个字段 
					String slot = member.attributeValue("slot");//对应词槽 作为查找该条件的键
					String type = member.attributeValue("type");//值的数据类型 
					
					String comment = member.attributeValue("comment");
					String defaultValue = member.getTextTrim();
					String dateFormat = member.attributeValue("date-format");
					String dateRef = member.attributeValue("date-ref");
					String codeType = member.attributeValue("code-type");
					Boolean required = member.attributeValue("required")==null?
							Boolean.FALSE:
							Boolean.valueOf(member.attributeValue("required"));
	
					
					String useClass = member.attributeValue("class");
					String useMethod = member.attributeValue("method");
					
					List<String> condition = Lists.newArrayList();
					QueryBO qbo = new QueryBO(key, slot, type, condition);
					if(StringUtils.isNotBlank(dateFormat))
						qbo.setDateFormat(dateFormat);
					if(StringUtils.isNotBlank(dateRef))
						qbo.setDateRef(dateRef);
					if(StringUtils.isNotBlank(comment))
						qbo.setComment(comment);
					if(StringUtils.isNotBlank(codeType))
						qbo.setCodeType(codeType);
					if(required!=null)
						qbo.setRequired(required);
					if(StringUtils.isNotBlank(useClass))
						qbo.setUseClass(useClass);
					if(StringUtils.isNotBlank(useMethod))
						qbo.setUseMethod(useMethod);
					if(StringUtils.isNotBlank(defaultValue)){//默认值
						condition = Arrays.asList(defaultValue.split(","));
						qbo.setIsInUse(Boolean.TRUE);
					}
						
					queryConditionMap.put(slot, qbo);
					
					l.add(condition);
				}else if(tag.equals("value")){//常量字符串
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
	
	@Override
	public Object clone() {
		try{  
            //save the object to a byte array  
            ByteArrayOutputStream bout = new ByteArrayOutputStream();  
            ObjectOutputStream out = new ObjectOutputStream(bout);  
            out.writeObject(this);  
            out.close();  
              
            //read a clone of the object from byte array  
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());  
            ObjectInputStream in = new ObjectInputStream(bin);  
            Object ret = in.readObject();  
            in.close();  
              
            return ret;  
        }catch(Exception e){  
        	e.printStackTrace();
            return null;  
        }  
	}

	
	
}
