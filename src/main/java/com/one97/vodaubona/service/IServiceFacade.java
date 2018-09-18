package com.one97.vodaubona.service;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FeedBackRequest;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.LangRecoRequest;

public interface IServiceFacade {

    String fetchSongs();

    String recoFeedBack(FeedBackRequest feedBackRequest, String transID, String queryString);

    FinalResponseBean fetchSongs(IVRRequest ivrRequest, String transID);

    void loadCircleLanguageSet();

    void loadIVRResponseCache();

    void saveRecommendedRespose(IVRRequest ivrRequest, String type, String transID, String response,String jsonResponse);

    void insertFeedbackLog(FeedBackRequest feedBackRequest, String queryString, String jsonRequest, String transID,
        String response);

    void insertFailLog(CircleLang circlelang, String fetchSongs, int reqType, String msisdn, String txn_id);

	String languageReco(LangRecoRequest langRecoRequest, String transID);

	void saveLangRecoRespose(LangRecoRequest langRecoRequest, String transID,String jsonResponse);
}
