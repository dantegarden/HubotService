package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class SessionBotResLexicalAnalysis {
	/**当前term的基础粒度分词**/
	private List<String> basic_word;
	/**NER类型**/
	private String type;
	/**当前term**/
	private String term;
	/**
	 * 当前term的重要性
	 * 所有term重要性之和为1**/
	private float weight;
	public List<String> getBasic_word() {
		return basic_word;
	}
	public void setBasic_word(List<String> basic_word) {
		this.basic_word = basic_word;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public SessionBotResLexicalAnalysis(List<String> basic_word, String type,
			String term, float weight) {
		super();
		this.basic_word = basic_word;
		this.type = type;
		this.term = term;
		this.weight = weight;
	}
	public SessionBotResLexicalAnalysis() {
		super();
	}
	
	
}
