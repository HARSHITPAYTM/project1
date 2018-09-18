package com.one97.vodaubona.beans;

public class IVRRequest {
    private String msisdn;
    private String circle;
    private String isUserSelectedLang;
    private String language;
    private String serviceId;
    private String txnId;
    private String isSubscribed;
    private String subscriptionClass;
    private String categorySongs;
    private String noOfSubCategories;
    private long timeTaken;

    public IVRRequest(String msisdn, String circle, String isUserSelectedLang, String language, String serviceId,
        String txnId, String isSubscribed, String subscriptionClass,String categorySongs,String noOfSubCategories) {
        super();
        this.msisdn = msisdn;
        this.circle = circle;
        this.isUserSelectedLang = isUserSelectedLang;
        this.language = language;
        this.serviceId = serviceId;
        this.txnId = txnId;
        this.isSubscribed = isSubscribed;
        this.subscriptionClass = subscriptionClass;
        this.categorySongs = categorySongs;
        this.noOfSubCategories = noOfSubCategories;
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

    public String getIsUserSelectedLang() {
        return isUserSelectedLang;
    }

    public void setIsUserSelectedLang(String isUserSelectedLang) {
        this.isUserSelectedLang = isUserSelectedLang;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getSubscriptionClass() {
        return subscriptionClass;
    }

    public void setSubscriptionClass(String subscriptionClass) {
        this.subscriptionClass = subscriptionClass;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IVRRequest [msisdn=");
        builder.append(msisdn);
        builder.append(", circle=");
        builder.append(circle);
        builder.append(", isUserSelectedLang=");
        builder.append(isUserSelectedLang);
        builder.append(", language=");
        builder.append(language);
        builder.append(", serviceId=");
        builder.append(serviceId);
        builder.append(", txnId=");
        builder.append(txnId);
        builder.append(", isSubscribed=");
        builder.append(isSubscribed);
        builder.append(", subscriptionClass=");
        builder.append(subscriptionClass);
        builder.append(", categorySongs=");
        builder.append(categorySongs);
        builder.append(", noOfSubCategories=");
        builder.append(noOfSubCategories);
        builder.append("]");
        return builder.toString();
    }

    public String[] getRequestParamArray() {
        return new String[] { msisdn, circle, isUserSelectedLang, language, serviceId, txnId, isSubscribed,
            subscriptionClass, categorySongs, noOfSubCategories};
    }

	/**
	 * @return the categorySongs
	 */
	public String getCategorySongs() {
		return categorySongs;
	}

	/**
	 * @param categorySongs the categorySongs to set
	 */
	public void setCategorySongs(String categorySongs) {
		this.categorySongs = categorySongs;
	}

	/**
	 * @return the noOfSubCategories
	 */
	public String getNoOfSubCategories() {
		return noOfSubCategories;
	}

	/**
	 * @param noOfSubCategories the noOfSubCategories to set
	 */
	public void setNoOfSubCategories(String noOfSubCategories) {
		this.noOfSubCategories = noOfSubCategories;
	}

}
