package com.dvt.HubotService.business.main.dto;

public class SessionSchemaSlot {
	/**原始词槽值*/
	private String original_word;
	/**词槽置信度，取值0~100**/
	private float confidence;
	/**词槽长度**/
	private int length;
	/**词槽起始位置**/
	private int begin;
	/**词槽归一后的值  如果没有默认为空**/
	private String normalized_word;
	/**
	 * 词槽类型
		qid:对应系统对问题的唯一表示
		faq_question:对应用户的问题
		faq_answer:对一个用户的答案
	 * **/
	private String type;
	/**
	 * 那一轮session中被更新的
	       默认为空**/
	private int session_offset;
	/**
	 * 更新的操作是什么
		add/update**/
	private String merge_method;
	
	public String getOriginal_word() {
		return original_word;
	}
	public void setOriginal_word(String original_word) {
		this.original_word = original_word;
	}
	public float getConfidence() {
		return confidence;
	}
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	public String getNormalized_word() {
		return normalized_word;
	}
	public void setNormalized_word(String normalized_word) {
		this.normalized_word = normalized_word;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSession_offset() {
		return session_offset;
	}
	public void setSession_offset(int session_offset) {
		this.session_offset = session_offset;
	}
	public String getMerge_method() {
		return merge_method;
	}
	public void setMerge_method(String merge_method) {
		this.merge_method = merge_method;
	}
	public SessionSchemaSlot(String original_word, float confidence,
			int length, int begin, String normalized_word, String type,
			int session_offset, String merge_method) {
		super();
		this.original_word = original_word;
		this.confidence = confidence;
		this.length = length;
		this.begin = begin;
		this.normalized_word = normalized_word;
		this.type = type;
		this.session_offset = session_offset;
		this.merge_method = merge_method;
	}
	public SessionSchemaSlot() {
		super();
	}
	
	
}
