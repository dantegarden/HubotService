package com.dvt.HubotService.business.main.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dvt.HubotService.business.main.dto.ConfigBO;
import com.dvt.HubotService.business.main.dto.EntryBO;
import com.dvt.HubotService.business.main.dto.HubotDTO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;
import com.dvt.HubotService.business.main.dto.ReqParam;
import com.dvt.HubotService.business.main.dto.SessionAction;
import com.dvt.HubotService.business.main.dto.SessionDTO;
import com.dvt.HubotService.business.main.dto.SessionSchema;
import com.dvt.HubotService.business.main.dto.SessionSchemaSlot;
import com.dvt.HubotService.business.main.service.AuthService;
import com.dvt.HubotService.business.main.service.CustomTimeService;
import com.dvt.HubotService.business.main.service.UnitService;
import com.dvt.HubotService.business.main.slot.global.UserDefinedSlotForCondition;
import com.dvt.HubotService.business.main.slot.global.UserDefinedSlotForEntry;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.entity.Result;
import com.dvt.HubotService.commons.exception.OApiException;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.dvt.HubotService.commons.utils.HttpHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.dvt.HubotService.commons.utils.JsonValidator;
import com.dvt.HubotService.commons.utils.WebAppCtxHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.time.nlp.TimeUnit;

/**
 * 000 没有意图
 * 003 进入模块
 * 011 查询
 * 012 新建
 * 020 澄清
 * 030 问候
 * */

@Controller
@RequestMapping("/ai")
public class AiController {
	
	private static final Logger logger = LoggerFactory.getLogger(AiController.class);
	private static final String token = "ROCKET_TOKEN";
	
	@Autowired
	private AuthService authService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private CustomTimeService customTimeService;
	
	
	@RequestMapping("test")
	@ResponseBody
	public Result test(HttpServletRequest request) throws OApiException, IOException {
		String myMes = request.getParameter("myMes");//"我要加班。从今晚6点到9点。";
		String userid = request.getParameter("userid");//"testUserId";
		String sceneid = request.getParameter("sceneid");//"testUserId";
		//String scene_id = "17699";//加班
		return analysis(myMes, userid, sceneid);
	}
	
	/**
	 * 正式接口
	 * **/
	@RequestMapping(value="show", method = RequestMethod.POST)
	@ResponseBody
	public Result show(HttpServletRequest request) throws OApiException, IOException {
		Map<String,String> params = Maps.newHashMap();
		
		Enumeration pnu=request.getParameterNames(); 
		while(pnu.hasMoreElements()){  
			String paraName=(String)pnu.nextElement();  
			if(new JsonValidator().validate(paraName)){
				System.out.println("接收到来自客户端的JSON数据:"+ paraName);
				HubotDTO hubotMsg = JsonUtils.jsonToJavaBean(paraName, HubotDTO.class);
				try {
					//TODO 1.分词  语义解析
					String myMes = hubotMsg.getMessage();
					String user_id = hubotMsg.getUserid();
					String scene_id = hubotMsg.getSceneid();
					return this.analysis(myMes, user_id, scene_id);
					
				} catch (Exception e) {
					e.printStackTrace();
					return new Result(500, e.getMessage(), null);
				}
			}
		}
		
		return new Result(500, "未接收到请求参数", null);
		
	}
	
	/**百度AI分析
	 * @throws ClassNotFoundException **/
	private Result analysis(String myMes, String user_id, String scene_id) throws OApiException, IOException {
		
		//看看是否这个用户是否在会话中，没有就传空
		String session_id = "";
		String sessionKey = user_id+"_"+scene_id;
		if(GlobalConstants.sceneWithUserSessions.containsKey(sessionKey)){
			session_id = GlobalConstants.sceneWithUserSessions.get(sessionKey);
		}
		
		//鉴权，驱动ai
		String accessToken = authService.getAuthToken();
		String backJson = unitService.utterance(accessToken, scene_id, session_id, myMes);
		System.out.println(backJson);
		SessionDTO mySession = JsonUtils.jsonToJavaBean(backJson, SessionDTO.class);
		if(mySession.getResult()!=null){
			//返回正确
			
			//拿到百度AI识别出来的会话id  更新会话缓存
			session_id = mySession.getResult().getSession_id();
			GlobalConstants.sceneWithUserSessions.put(sessionKey, session_id);
			
			//会话，语义解析的结果是否已经满足请求被调系统的条件
			boolean reqflag = Boolean.FALSE;
			String say = "";//机器人回答的文本
			String actionTypeCode = "000";
			
			String actionId = "";
			
			List<SessionAction> action_list = mySession.getResult().getAction_list();
			if(CollectionUtils.isNotEmpty(action_list)){
				SessionAction action = action_list.get(0);
				actionId = action.getAction_id();
				if("fail_action".equals(actionId)){//没有命中任何对话单元
					if(StringUtils.isNotBlank(action.getSay())){
						say = action.getSay();
					}
				}else if(mySession.getResult().getQu_res().getIntent_candidates().get(0).getIntent_confidence() < 75.0){
					//命中了某个对话单元，但可信度不足阈值
					say = "我不知道应该怎么回答您。";
					//迫使会话结束，移除会话缓存
					GlobalConstants.sceneWithUserSessions.remove(sessionKey);
				}else{//命中了某个对话单元且可信
					switch (action.getAction_type().getAct_type()) {
					case "clarify":{//需要澄清
						switch (action.getAction_type().getAct_target()) {
							case "slot":{
								//需要澄清词槽
								System.out.println("待澄清的词槽：" + action.getAction_type().getAct_target_detail());
								say = action.getSay();
								actionTypeCode = "020";
								break;
							}
							case "intent":{
								//TODO 需要澄清意图
								break;
							}
							default:
								break;
						}
						break;
					}
					case "satisfy":{//已经满足触发条件了，识别完成
						reqflag = Boolean.TRUE;
						//因为本次会话已经满足触发条件了，会话结束，移除会话缓存
						GlobalConstants.sceneWithUserSessions.remove(sessionKey);
						actionTypeCode = action.getMain_exe();
						if(StringUtils.isNotBlank(action.getSay())){
							say = action.getSay();
						}
						break;
					}
					case "guide":{//TODO 引导
						
						break;
					}
					case "faqguide":{//TODO faq引导
						
						break;
					}
					default:
						break;
				}	
					
				}
			}
			
			//语义解析满足被调系统的需求
			if(reqflag){
				//获得百度AI识别出来的 意图 与 词槽
				SessionSchema schema = mySession.getResult().getSchema();
				String intent = schema.getCurrent_qu_intent();
				List<SessionSchemaSlot> slots = schema.getBot_merged_slots();
				
				//通过意图 从配置文件中 寻找对应的被调系统的配置信息
				if(GlobalConstants.interfaces.containsKey(intent)){
					//共用内存地址，在多轮对话中会有问题 应使用深拷贝
					HubotInterface myHif = (HubotInterface)GlobalConstants.interfaces.get(intent).clone();
					
					for (SessionSchemaSlot sss : slots) {
						String wordSlot = sss.getType();
						if(myHif.getArgEntryMap().containsKey(wordSlot)){//修改系操作
							EntryBO ebo = myHif.getArgEntryMap().get(wordSlot);
							ebo.setIsInUse(Boolean.TRUE);//标记使用
							ebo.setTempValue(sss.getOriginal_word());
						}else if(myHif.getQueryConditionMap().containsKey(wordSlot)){//查询条件
							QueryBO qbo = myHif.getQueryConditionMap().get(wordSlot);
							qbo.setIsInUse(Boolean.TRUE);
							qbo.setTempValue(sss.getOriginal_word());
						}
					}
					
					//先处理键值对类型
					List<EntryBO> inUsedEntryBOVars = Lists.newArrayList(myHif.getArgEntryMap().values());//找出所有被标注使用的配置项
					inUsedEntryBOVars = inUsedEntryBOVars.stream().filter(_ebo -> _ebo.getIsInUse()).collect(Collectors.toList());
					for (EntryBO ebo : inUsedEntryBOVars) {
						switch (ebo.getType()) {
							case "string":{
								if(StringUtils.isNotBlank(ebo.getUseClass())){
									ebo.getEntry().setValue(this.invokeUserDefinedMethod(myHif, ebo));
								}else{
									ebo.getEntry().setValue(ebo.getTempValue());
								}
								break;
							}
							case "boolean":{
								if(StringUtils.isNotBlank(ebo.getUseClass())){
									ebo.getEntry().setValue(this.invokeUserDefinedMethod(myHif, ebo));
								}else{
									//TODO 统一处理Boolean类型的方法
									ebo.getEntry().setValue(ebo.getTempValue());
								}
								break;
							}
							case "date":{
								if(StringUtils.isNotBlank(ebo.getUseClass())){
									ebo.getEntry().setValue(this.invokeUserDefinedMethod(myHif, ebo));
								}else{
									//统一处理Date类型的方法
									ebo.getEntry().setValue(customTimeService.controlTimeVar(myHif, ebo));
								}
								break;
							}
							default:
								break;
						}
					}	
					
					//再处理数组类型
					List<QueryBO> inUsedQueryBOVars = Lists.newArrayList(myHif.getQueryConditionMap().values());//找出所有被标注使用的配置项
					inUsedQueryBOVars = inUsedQueryBOVars.stream().filter(_qbo -> _qbo.getIsInUse()).collect(Collectors.toList());
					for (QueryBO qbo : inUsedQueryBOVars) {
						switch (qbo.getType()) {
							case "string":{
								if(StringUtils.isNotBlank(qbo.getUseClass())){
									qbo.getCondition().addAll(this.invokeUserDefinedMethod(myHif, qbo));
								}else{
									qbo.getCondition().add(qbo.getKey());
									qbo.getCondition().add("ilike");
									qbo.getCondition().add(qbo.getTempValue());
								}
								break;
							}
							case "selection":{
								if(StringUtils.isNotBlank(qbo.getUseClass())){
									qbo.getCondition().addAll(this.invokeUserDefinedMethod(myHif, qbo));
								}else{
									qbo.getCondition().add(qbo.getKey());
									qbo.getCondition().add("=");
									qbo.getCondition().add(qbo.getTempValue());
								}
								break;
							}
							case "boolean":{
								if(StringUtils.isNotBlank(qbo.getUseClass())){
									qbo.getCondition().addAll(this.invokeUserDefinedMethod(myHif, qbo));
								}else{
									//TODO boolean类型的默认处理方法
									qbo.getCondition().add(qbo.getKey());
									qbo.getCondition().add("=");
									qbo.getCondition().add(qbo.getTempValue());
								}
								break;
							}
							case "date":{
								if(StringUtils.isNotBlank(qbo.getUseClass())){
									qbo.getCondition().addAll(this.invokeUserDefinedMethod(myHif, qbo));
								}else{
									//统一处理Date类型的方法
									qbo.getCondition().add(qbo.getKey());
									qbo.getCondition().add("=");
									qbo.getCondition().add(customTimeService.controlTimeVar(myHif, qbo));
								}
								break;
							}
							default:
								break;
						}
					}
					//检查是否必要变量都被赋值了
					if(isRequestReady(myHif)){
						//清理空的condition
						if(myHif.getPostObject()!=null && myHif.getPostObject().containsKey("domain")){//配置了domain
							List<List<String>> conditions = (List<List<String>>) myHif.getPostObject().get("domain");
							conditions = conditions.stream().filter(condition -> !CollectionUtils.isEmpty(condition)).collect(Collectors.toList());
							myHif.getPostObject().put("domain",conditions);
						}
						
						String entryUrl = myHif.getEntryUrl();//被调系统的URL
						return Result.getInstance(200, actionTypeCode, myHif);
					}
				}else{
					//意图 不在 被调系统的配置文件里
					//认为该意图是普通问答（百度ai自带）
					if(actionId.toUpperCase().indexOf("FAQ_CTRL_UI")>-1){//操作ui
						return Result.getInstance(200, "003", actionTypeCode);//这里的actionTypeCode是在百度上约定的返回值
					}else if(actionId.toUpperCase().indexOf("BUILT_FAQ_")>-1){//问候
						return Result.getInstance(200, "030", "{\"msg\": \""+say+"\"}");
					}
				}
			}else{
				//回话
				return new Result(200, say, null);
			}
			
			
			
		}else{
			//返回错误
			String errorKey = mySession.getError_code();
			if(StringUtils.isNotBlank(errorKey)){
				String errorReason = GlobalConstants.baiduUnitErrKv.get(errorKey);
				return new Result(500, errorReason, null);
			}
		}
		return null;
	}
	
	/**被调系统所需的 参数是否齐备**/
	private boolean isRequestReady(HubotInterface myHif){
		boolean readyFlag = Boolean.TRUE; 
		Iterator<String> iterator = myHif.getArgEntryMap().keySet().iterator();
    	while (iterator.hasNext()) {
    		String slot = iterator.next();
    		EntryBO ebo = myHif.getArgEntryMap().get(slot);
    		if(ebo.getRequired() && ebo.getEntry().getValue()==null){
    			//是必要的，但没赋值
    			readyFlag = Boolean.FALSE;
    			break;
    		}
    	}
    	if(readyFlag){
    		iterator = myHif.getQueryConditionMap().keySet().iterator();
        	while (iterator.hasNext()) {
        		String slot = iterator.next();
        		QueryBO qbo = myHif.getQueryConditionMap().get(slot);
        		if(qbo.getRequired() && CollectionUtils.isEmpty(qbo.getCondition())){
        			//是必要的，但没赋值
        			readyFlag = Boolean.FALSE;
        			break;
        		}
        	}
    	}
    	return readyFlag;
	}
	
	
	@SuppressWarnings("finally")
	public String invokeUserDefinedMethod(HubotInterface myHif, EntryBO ebo){
		String methodResult = "";
		try{
			String methodName = StringUtils.isNotBlank(ebo.getUseMethod())?ebo.getUseMethod():"dispose";
			UserDefinedSlotForEntry slotBean = (UserDefinedSlotForEntry) WebAppCtxHolder.getSpringWebAppCtx().getBean(ebo.getUseClass());
			methodResult = slotBean.dispose(myHif, ebo);
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			return methodResult;
		}
	}
	
	@SuppressWarnings("finally")
	public List<String> invokeUserDefinedMethod(HubotInterface myHif, QueryBO qbo){
		List<String> methodResults = Lists.newArrayList();
		try{
			String methodName = StringUtils.isNotBlank(qbo.getUseMethod())?qbo.getUseMethod():"dispose";
			UserDefinedSlotForCondition slotBean = (UserDefinedSlotForCondition) WebAppCtxHolder.getSpringWebAppCtx().getBean(qbo.getUseClass());
			methodResults = slotBean.dispose(myHif, qbo);
		}catch(Exception e){
			e.printStackTrace();
		}  finally{
			return methodResults;
		}
	}
}
