package com.dvt.HubotService.business.main.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr.Unit;
import com.dvt.HubotService.business.main.dto.EntryBO;
import com.dvt.HubotService.business.main.dto.HubotDTO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.ReqParam;
import com.dvt.HubotService.business.main.dto.SessionAction;
import com.dvt.HubotService.business.main.dto.SessionDTO;
import com.dvt.HubotService.business.main.dto.SessionSchema;
import com.dvt.HubotService.business.main.dto.SessionSchemaSlot;
import com.dvt.HubotService.business.main.service.AuthService;
import com.dvt.HubotService.business.main.service.UnitService;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.entity.Result;
import com.dvt.HubotService.commons.exception.OApiException;
import com.dvt.HubotService.commons.exception.OApiResultException;
import com.dvt.HubotService.commons.utils.CommonHelper;
import com.dvt.HubotService.commons.utils.HttpHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.dvt.HubotService.commons.utils.JsonValidator;
import com.dvt.HubotService.commons.utils.RegexUtils;
import com.dvt.HubotService.commons.utils.XmlUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.time.nlp.TimeUnit;

@Controller
@RequestMapping("/myhubot")
public class MyController {
	
	private static final Logger logger = LoggerFactory.getLogger(MyController.class);
	private static final String token = "ROCKET_TOKEN";
	
	@Autowired
	private AuthService authService;
	@Autowired
	private UnitService unitService;
	
	
	@RequestMapping("test")
	@ResponseBody
	public Result test(HttpServletRequest request) throws OApiException, IOException {
		String myMes = request.getParameter("myMes");//"我要加班。从今晚6点到9点。";
		String userid = request.getParameter("userid");//"testUserId";
		String roomid = request.getParameter("roomid");//"testRoomId";
		return analysis(myMes, userid, roomid);
	}
	
	/**百度AI分析**/
	private Result analysis(String myMes, String userid, String roomid) throws OApiException, IOException{
		
		//看看是否这个用户是否在会话中，没有就传空
		String session_id = "";
		if(GlobalConstants.aiSessions.containsKey(userid)){
			session_id = GlobalConstants.aiSessions.get(userid);
		}
		
		//鉴权，驱动ai
		String accessToken = authService.getAuthToken();
		String backJson = unitService.utterance(accessToken, "17699", session_id, myMes);
		System.out.println(backJson);
		SessionDTO mySession = JsonUtils.jsonToJavaBean(backJson, SessionDTO.class);
		if(mySession.getResult()!=null){
			//返回正确
			
			//拿到百度AI识别出来的会话id  更新会话缓存
			session_id = mySession.getResult().getSession_id();
			GlobalConstants.aiSessions.put(userid, session_id);
			
			//会话，语义解析的结果是否已经满足请求被调系统的条件
			boolean reqflag = Boolean.FALSE;
			String say = "";//机器人回答的文本
			
			List<SessionAction> action_list = mySession.getResult().getAction_list();
			if(CollectionUtils.isNotEmpty(action_list)){
				SessionAction action = action_list.get(0);
				String actionId = action.getAction_id();
				if("fail_action".equals(actionId)){//没有命中任何对话单元
					if(StringUtils.isNotBlank(action.getSay())){
						say = action.getSay();
					}
				}else{//命中了某个对话单元
					switch (action.getAction_type().getAct_type()) {
						case "clarify":{//需要澄清
							switch (action.getAction_type().getAct_target()) {
								case "slot":{
									//需要澄清词槽
									System.out.println("待澄清的词槽：" + action.getAction_type().getAct_target_detail());
									say = action.getSay();
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
							GlobalConstants.aiSessions.remove(userid);
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
					HubotInterface myHif = GlobalConstants.interfaces.get(intent);
					for (SessionSchemaSlot sss : slots) {
						String wordSlot = sss.getType();
						if(myHif.getArgEntryMap().containsKey(wordSlot)){
							EntryBO ebo = myHif.getArgEntryMap().get(wordSlot);
							ebo.getEntry().setValue(sss.getOriginal_word());//先赋值原词
						}
					}
					
					//把date类型的变量集中处理一下
					controlTimeVar(myHif, myMes);
					//TODO 把boolean的变量集中处理一下
					//TODO 把selection的变量集中处理一下
					
					//检查是否必要变量都被赋值了
					if(isRequestReady(myHif)){
						//计算鉴权
						String nonceStr = "abcdefg";
						String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
						String signature = sign(nonceStr, timeStamp);
						
						String entryUrl = myHif.getEntryUrl();//被调系统的URL
						//TODO 替换 userid 来自机器人发送
						ReqParam rp = new ReqParam(nonceStr, timeStamp, signature, userid, roomid, myHif);
						
						//调接口  处理结果由被调系统回复
						Map<String,String> params = Maps.newHashMap();
						params.put("params", JsonUtils.JavaBeanToJson(rp));
						System.out.println(JsonUtils.JavaBeanToJson(rp));
						//成功执行时的回话
						say = myHif.getAnswer();
						//TODO 解开注释
						HttpHelper.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
						String responseJson = HttpHelper.startPost(entryUrl, params);
						System.out.println(responseJson);
						return new Result(1, say, null);
					}
				}else{
					//意图 不在 被调系统的配置文件里
					//认为该意图是普通问答（百度ai自带）
					return new Result(1, say, null);
				}
			}else{
				//回话
				return new Result(1, say, null);
			}
			
			
			
		}else{
			//返回错误
			String errorKey = mySession.getError_code();
			if(StringUtils.isNotBlank(errorKey)){
				String errorReason = GlobalConstants.baiduUnitErrKv.get(errorKey);
				return new Result(2, errorReason, null);
			}
		}
		return null;
	}
	
	/**
	 * 正式接口
	 * **/
	@RequestMapping(value="show",method = RequestMethod.POST)
	@ResponseBody
	public Result  show(HttpServletRequest request) throws OApiException, IOException {
		Map<String,String> params = Maps.newHashMap();
		Enumeration pnu=request.getParameterNames(); 
		while(pnu.hasMoreElements()){  
			String paraName=(String)pnu.nextElement();  
			if(new JsonValidator().validate(paraName)){
				System.out.println("接收到来自机器人的JSON数据:"+ paraName);
				HubotDTO hubotMsg = JsonUtils.jsonToJavaBean(paraName, HubotDTO.class);
				try {
					//TODO 1.分词  语义解析
					String myMes = hubotMsg.getMessage();
					String userid = hubotMsg.getUserid();
					String roomid = hubotMsg.getRoomid();
					return this.analysis(myMes, userid, roomid);
					
				} catch (Exception e) {
					e.printStackTrace();
					return new Result(2, e.getMessage(), null);
				}
			}
		}
		
		return new Result(2, "未接收到请求参数", null);
		
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
    	return readyFlag;
	}
	
	private void controlTimeVar(HubotInterface myHif, String myMes){
		List<EntryBO> timeVars = Lists.newArrayList();
		//找出所需的所有时间变量
		Iterator<String> iterator = myHif.getArgEntryMap().keySet().iterator();
    	while (iterator.hasNext()) {
    		String slot = iterator.next();
    		EntryBO ebo = myHif.getArgEntryMap().get(slot);
    		if("date".equals(ebo.getType())){
    			timeVars.add(ebo);
    		}
    	}
    	if(CollectionUtils.isNotEmpty(timeVars)){
    		if(timeVars.size()==1){
    			String timeOrginStr = (String)timeVars.get(0).getEntry().getValue();
    			GlobalConstants.normalizer.parse(timeOrginStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			String timeNlpTimeStr = CommonHelper.date2Str(unit[0].getTime(), CommonHelper.DF_DATE_TIME);
				timeVars.get(0).getEntry().setValue(timeNlpTimeStr);
    		}else{//多于一个
    			//time-nlp需要让它们出现在同一句话里
    			String sampleOrignStr = "";
    			for (int i = 0; i < timeVars.size(); i++) {
    				EntryBO ebo = timeVars.get(i);
    				sampleOrignStr+= (String)ebo.getEntry().getValue()+",";
    			}
    			sampleOrignStr = sampleOrignStr.substring(0,sampleOrignStr.length()-1);
    			
    			GlobalConstants.normalizer.parse(sampleOrignStr);
    			TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();//time-nlp 识别
    			List<String> unitStr = GlobalConstants.normalizer.getTimeStr();//time-nlp 被识别的字段
    			if(unit.length == unitStr.size()){
    	    		for (EntryBO ebo : timeVars) {
    	    			for (int i = 0; i < unitStr.size(); i++) {
    						if(unitStr.get(i).contains((String)ebo.getEntry().getValue())){
    							String timeNlpTimeStr = CommonHelper.date2Str(unit[i].getTime(), CommonHelper.DF_DATE_TIME);
    							ebo.getEntry().setValue(timeNlpTimeStr);
    						}
    					}
    				}
    	    	}
    		}
    		
	    	
    	}
    	
    	
	}
	
	private String sign(String nonceStr, String timeStamp) throws OApiException {
		
		String plain = this.token + "." + timeStamp + "." + nonceStr;

		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return bytesToHex(sha1.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new OApiResultException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new OApiResultException(e.getMessage());
		}
	}
	
	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
