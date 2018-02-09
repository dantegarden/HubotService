package com.dvt.HubotService.business.main.dto;

public class ReqParam {

	private String nonce;
	private String timestamp;
	private String signature;
	private String userid;

	private HubotInterface hif;

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public HubotInterface getHif() {
		return hif;
	}

	public void setHif(HubotInterface hif) {
		this.hif = hif;
	}

	public ReqParam(String nonce, String timestamp, String signature,
			String userid, HubotInterface hif) {
		super();
		this.nonce = nonce;
		this.timestamp = timestamp;
		this.signature = signature;
		this.userid = userid;
		this.hif = hif;
	}

	public ReqParam() {
		super();
	}
	
	
}
