package com.one97.vodaubona.beans;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder(value = { "feedbackTime", "msisdn", "circle", "isUserSelectedLang", "language", "serviceId",
    "txnId", "isSubscribed", "subscriptionClass", "totalSOU", "userTrail", "songPlayed" })
public class FeedBackRequest {

    private String feedbackTime;
    private String msisdn;
    private String circle;
    private String isUserSelectedLang;
    private String language;
    private String serviceId;
    private String txnId;
    private String isSubscribed;
    private String subscriptionClass;
    private String totalSOU;
    private String userTrail;
    private List<FeedbackCategory> songPlayed;

    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
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

    public String getTotalSOU() {
        return totalSOU;
    }

    public void setTotalSOU(String totalSOU) {
        this.totalSOU = totalSOU;
    }

    public String getUserTrail() {
        return userTrail;
    }

    public void setUserTrail(String userTrail) {
        this.userTrail = userTrail;
    }

    public List<FeedbackCategory> getSongPlayed() {
        return songPlayed;
    }

    public void setSongPlayed(List<FeedbackCategory> songPlayed) {
        this.songPlayed = songPlayed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FeedBackRequest [feedbackTime=");
        builder.append(feedbackTime);
        builder.append(", msisdn=");
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
        builder.append(", totalSOU=");
        builder.append(totalSOU);
        builder.append(", userTrail=");
        builder.append(userTrail);
        builder.append(", songPlayed=");
        builder.append(songPlayed);
        builder.append("]");
        return builder.toString();
    }

}
