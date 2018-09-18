package com.one97.vodaubona.beans;


public class LangRecoRequest {
	
	private String msisdn;
	private String circle;
	private String serviceId;
	private String txnId;
	private long responseTime;
	
	public LangRecoRequest() {
	}
	public LangRecoRequest(String msisdn, String circle, String serviceId,
			String txnId) {
		this.msisdn = msisdn;
		this.circle = circle;
		this.serviceId = serviceId;
		this.txnId = txnId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	
	@Override
	public String toString() {
		return "LangRecoRequest [msisdn=" + msisdn + ", circle=" + circle
				+ ", serviceId=" + serviceId + ", txnId=" + txnId
				+ ", responseTime=" + responseTime + "]";
	}
	
	

}
