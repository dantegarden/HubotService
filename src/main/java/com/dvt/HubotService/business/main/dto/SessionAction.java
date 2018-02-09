package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class SessionAction {
	/**
	 * 动作名称，当为fail_action时表示未能命中任何意图
	 * **/
	private String action_id;
	/**结果置信度 **/
	private float confidence;
	/**执行主函数**/
	private String main_exe;
	/**返回的话术**/
	private String say; 
	/**动作详细**/
	private SessionActionType action_type;
	/**
	 * 参数  
	 * 只当action是做意图或词槽澄清的时候，这里才会赋值；"string"通常是意图or词槽名称
	 * **/
	private List<String> arg_list;
	/**
	 * 动作的引导选项。
	 * 如果当前动作有引导，该域存在，faq问答中存在多个question需要进一步澄清时，也在此存放
	 * **/
	private List<SessionActionHint> hint_list;
	private List<String> exe_status;//保留字段
	private Object code_actions;//保留字段
	
	public String getAction_id() {
		return action_id;
	}
	public void setAction_id(String action_id) {
		this.action_id = action_id;
	}
	public float getConfidence() {
		return confidence;
	}
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	public String getMain_exe() {
		return main_exe;
	}
	public void setMain_exe(String main_exe) {
		this.main_exe = main_exe;
	}
	public String getSay() {
		return say;
	}
	public void setSay(String say) {
		this.say = say;
	}
	
	public SessionActionType getAction_type() {
		return action_type;
	}
	public void setAction_type(SessionActionType action_type) {
		this.action_type = action_type;
	}
	public List<String> getArg_list() {
		return arg_list;
	}
	public void setArg_list(List<String> arg_list) {
		this.arg_list = arg_list;
	}
	public List<SessionActionHint> getHint_list() {
		return hint_list;
	}
	public void setHint_list(List<SessionActionHint> hint_list) {
		this.hint_list = hint_list;
	}
	public List<String> getExe_status() {
		return exe_status;
	}
	public void setExe_status(List<String> exe_status) {
		this.exe_status = exe_status;
	}
	public Object getCode_actions() {
		return code_actions;
	}
	public void setCode_actions(Object code_actions) {
		this.code_actions = code_actions;
	}
	public SessionAction(String action_id, float confidence, String main_exe,
			String say, SessionActionType action_type,
			List<String> arg_list, List<SessionActionHint> hint_list,
			List<String> exe_status, Object code_actions) {
		super();
		this.action_id = action_id;
		this.confidence = confidence;
		this.main_exe = main_exe;
		this.say = say;
		this.action_type = action_type;
		this.arg_list = arg_list;
		this.hint_list = hint_list;
		this.exe_status = exe_status;
		this.code_actions = code_actions;
	}
	public SessionAction() {
		super();
	}
	
	
}
