package com.dvt.HubotService.business.main.dto;

public class SessionActionType {
	/**
	 *  动作类型，取值clarify/satisfy/guide
		clarify： 澄清
		satisfy： 满足
		guide： 引导
		faqguide： faq引导
	 * **/
	private String act_type;
	/**
	 *  动作的目标，取值intent/slot/slot_type，仅当act_type==clarify时有值；其他情况为空
		intent： 意图澄清时
		slot：对某个词槽，用户期望有值但返回的结果中没有填值 or 返回多个置信度接近的意图候选，且意图名相同，且出现同个词槽填了不同的值
		slot_type：返回多个置信度接近的意图候选，且意图名相同，且出现同个值填到了不同的词槽
	 * **/
	private String act_target;
	/**
	 * 动作目标详细内容，仅当act_type==clarify且act_target==slot时填词槽名称；其他情况为空
	 * **/
	private String act_target_detail;
	/**
	 * 默认空值，动作类型的详细内容。
	 * */
	private String action_type_detail;
	
	public String getAct_type() {
		return act_type;
	}
	public void setAct_type(String act_type) {
		this.act_type = act_type;
	}
	public String getAct_target() {
		return act_target;
	}
	public void setAct_target(String act_target) {
		this.act_target = act_target;
	}
	public String getAct_target_detail() {
		return act_target_detail;
	}
	public void setAct_target_detail(String act_target_detail) {
		this.act_target_detail = act_target_detail;
	}
	public String getAction_type_detail() {
		return action_type_detail;
	}
	public void setAction_type_detail(String action_type_detail) {
		this.action_type_detail = action_type_detail;
	}
	public SessionActionType(String act_type, String act_target,
			String act_target_detail, String action_type_detail) {
		super();
		this.act_type = act_type;
		this.act_target = act_target;
		this.act_target_detail = act_target_detail;
		this.action_type_detail = action_type_detail;
	}
	public SessionActionType() {
		super();
	}
	
	
}
