package com.dvt.HubotService.business.main.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.dvt.HubotService.commons.utils.CommonHelper;
import com.dvt.HubotService.commons.utils.CommonHelper.TimeUnit;

public class AuthDTO {
	private String access_token;
	private Long expires_in;//有效期
	private String session_secret;
	private String session_key;
	private String scope;
	private String refresh_token;
	private Date accessTime;//获取时间
	
	private String error;
	private String error_description;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}
	public AuthDTO(String access_token, Long expires_in) {
		super();
		this.access_token = access_token;
		this.expires_in = expires_in;
	}
	public Date getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}
	
	
	
	public String getSession_secret() {
		return session_secret;
	}
	public void setSession_secret(String session_secret) {
		this.session_secret = session_secret;
	}
	public String getSession_key() {
		return session_key;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public AuthDTO() {
		super();
	}
	
	
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	//验证是否过期
	public boolean checkTokenEffect(){
		boolean isEffect = Boolean.FALSE;
		if(this.accessTime!=null && StringUtils.isNotBlank(this.access_token)){
			long diff = CommonHelper.Timediff(new Date(), this.accessTime, TimeUnit.SECOND);
			if(diff > 0){
				isEffect = Boolean.TRUE;
			}
		}
		return isEffect;
	}
	
}
