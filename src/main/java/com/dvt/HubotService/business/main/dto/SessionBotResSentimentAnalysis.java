package com.dvt.HubotService.business.main.dto;

public class SessionBotResSentimentAnalysis {
	/***
	 * 情感分析倾向
	2表示正向，1表示中性，0表示负向
	 * **/
	private int label;
	/**
	 * 好评置信度 - 差评置信度
	越大表示分类结果的可靠性越高，0-1之间的值
	 * **/
	private float pval;
	
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public float getPval() {
		return pval;
	}
	public void setPval(float pval) {
		this.pval = pval;
	}
	public SessionBotResSentimentAnalysis(int label, float pval) {
		super();
		this.label = label;
		this.pval = pval;
	}
	public SessionBotResSentimentAnalysis() {
		super();
	}
	
}
