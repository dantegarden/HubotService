package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class SessionSchema {
	/**当前意图**/
	private String current_qu_intent;
	/**意图置信度**/
	private float intent_confidence;
	/**词槽列表**/
	private List<SessionSchemaSlot> bot_merged_slots;
	
	public String getCurrent_qu_intent() {
		return current_qu_intent;
	}
	public void setCurrent_qu_intent(String current_qu_intent) {
		this.current_qu_intent = current_qu_intent;
	}
	public float getIntent_confidence() {
		return intent_confidence;
	}
	public void setIntent_confidence(float intent_confidence) {
		this.intent_confidence = intent_confidence;
	}
	public List<SessionSchemaSlot> getBot_merged_slots() {
		return bot_merged_slots;
	}
	public void setBot_merged_slots(List<SessionSchemaSlot> bot_merged_slots) {
		this.bot_merged_slots = bot_merged_slots;
	}
	public SessionSchema(String current_qu_intent, float intent_confidence,
			List<SessionSchemaSlot> bot_merged_slots) {
		super();
		this.current_qu_intent = current_qu_intent;
		this.intent_confidence = intent_confidence;
		this.bot_merged_slots = bot_merged_slots;
	}
	public SessionSchema() {
		super();
	}
	
	
}
