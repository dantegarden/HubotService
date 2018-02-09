package com.dvt.HubotService.business.main.dto;

/**
 * @author HELP
 *
 */
public class SessionBotResIntentCandidateSlots {
	/**词槽置信度**/
	private float confidence;
	/**词槽长度**/
	private int length;
	/**偏移量**/
	private int offset;
	/**原始词槽值**/
	private String original_word;
	/**
	 * 词槽归一后的值
		如果没有默认为空*/
	private String normalized_word;
	/**词槽类型**/
	private String type;
	/**
	 * 词槽细化类型
		默认为空
	 * **/
	private String word_type;
	/**
	 * 	不置信澄清的功能：词槽是否需要澄清
		默认false
	 * **/
	private boolean need_clarify;
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
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getOriginal_word() {
		return original_word;
	}
	public void setOriginal_word(String original_word) {
		this.original_word = original_word;
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
	public String getWord_type() {
		return word_type;
	}
	public void setWord_type(String word_type) {
		this.word_type = word_type;
	}
	public boolean isNeed_clarify() {
		return need_clarify;
	}
	public void setNeed_clarify(boolean need_clarify) {
		this.need_clarify = need_clarify;
	}
	public SessionBotResIntentCandidateSlots(float confidence, int length,
			int offset, String original_word, String normalized_word,
			String type, String word_type, boolean need_clarify) {
		super();
		this.confidence = confidence;
		this.length = length;
		this.offset = offset;
		this.original_word = original_word;
		this.normalized_word = normalized_word;
		this.type = type;
		this.word_type = word_type;
		this.need_clarify = need_clarify;
	}
	public SessionBotResIntentCandidateSlots() {
		super();
	}
	
	
}
