package com.one97.vodaubona.daoImpl;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FeedBackRequest;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.LangRecoRequest;

public interface IUbonaDAO {

    void loadCircleLanguageSet();

    void loadSongsCache();

    void insertSongs(CircleLang circlelang, FinalResponseBean finalResponseBean);

    void saveRecommendedRespose(IVRRequest ivrRequest, String type, String transID, String response,String jsonResponse);

    void insertFeedbackLog(FeedBackRequest feedBackRequest, String queryString, String jsonRequest, String transID,
        String response);

    void insertFailLog(CircleLang circlelang, String response, int reqType, String msisdn, String txn_id);

	void saveRecommendedRespose(LangRecoRequest langRecoRequest,String transID, String jsonResponse);

}
