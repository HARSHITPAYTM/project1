package com.one97.vodaubona.handler;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FeedBackRequest;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.LangRecoRequest;

public interface IUbonaREHandler {

	FinalResponseBean fetchSongs(IVRRequest ivrRequest, String transID);

    FinalResponseBean fetchSongs(CircleLang circleLang);

    String recoFeedBack(FeedBackRequest feedBackRequest, String transID, String queryString);

	String languageReco(LangRecoRequest langRecoRequest, String transID);

}
