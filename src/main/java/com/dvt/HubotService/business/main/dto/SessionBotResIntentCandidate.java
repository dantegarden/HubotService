package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class SessionBotResIntentCandidate {
	/**附加信息**/
	private String extra_info;
	/**func功能 默认是空**/
	private String func_slot;
	/**意图**/
	private String intent;
	/**意图置信度**/
	private float intent_confidence;
	/**词槽列表***/
	private List<SessionBotResIntentCandidateSlots> slots;
	/**不置信澄清的功能：意图是否需要澄清默认为false**/
	private boolean intent_need_clarify;
	/**哪个qu出的结果**/
	private String from_who;
	/**匹配的信息  比如匹配了哪个模板**/
	private String match_info;
	
	public String getExtra_info() {
		return extra_info;
	}
	public void setExtra_info(String extra_info) {
		this.extra_info = extra_info;
	}
	public String getFunc_slot() {
		return func_slot;
	}
	public void setFunc_slot(String func_slot) {
		this.func_slot = func_slot;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public float getIntent_confidence() {
		return intent_confidence;
	}
	public void setIntent_confidence(float intent_confidence) {
		this.intent_confidence = intent_confidence;
	}
	public List<SessionBotResIntentCandidateSlots> getSlots() {
		return slots;
	}
	public void setSlots(List<SessionBotResIntentCandidateSlots> slots) {
		this.slots = slots;
	}
	public boolean isIntent_need_clarify() {
		return intent_need_clarify;
	}
	public void setIntent_need_clarify(boolean intent_need_clarify) {
		this.intent_need_clarify = intent_need_clarify;
	}
	public String getFrom_who() {
		return from_who;
	}
	public void setFrom_who(String from_who) {
		this.from_who = from_who;
	}
	public String getMatch_info() {
		return match_info;
	}
	public void setMatch_info(String match_info) {
		this.match_info = match_info;
	}
	public SessionBotResIntentCandidate(String extra_info, String func_slot,
			String intent, float intent_confidence,
			List<SessionBotResIntentCandidateSlots> slots,
			boolean intent_need_clarify, String from_who, String match_info) {
		super();
		this.extra_info = extra_info;
		this.func_slot = func_slot;
		this.intent = intent;
		this.intent_confidence = intent_confidence;
		this.slots = slots;
		this.intent_need_clarify = intent_need_clarify;
		this.from_who = from_who;
		this.match_info = match_info;
	}
	public SessionBotResIntentCandidate() {
		super();
	}
	
	
}
