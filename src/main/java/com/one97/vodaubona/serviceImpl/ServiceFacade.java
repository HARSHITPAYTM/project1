package com.one97.vodaubona.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FeedBackRequest;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.LangRecoRequest;
import com.one97.vodaubona.daoImpl.IUbonaDAO;
import com.one97.vodaubona.handler.IUbonaREHandler;
import com.one97.vodaubona.service.IServiceFacade;

@Service
public class ServiceFacade implements IServiceFacade {

    @Autowired
    private IUbonaREHandler iUbonaREHandler;

    @Autowired
    private IUbonaDAO ubonaDAO;

    @Override
    public FinalResponseBean fetchSongs(IVRRequest ivrRequest, String transID) {
        return iUbonaREHandler.fetchSongs(ivrRequest, transID);
    }

    @Override
    public String fetchSongs() {
        return null;
    }

    @Override
    public String recoFeedBack(FeedBackRequest feedBackRequest, String transID, String queryString) {
        return iUbonaREHandler.recoFeedBack(feedBackRequest, transID, queryString);
    }

    @Override
    public void loadCircleLanguageSet() {
        ubonaDAO.loadCircleLanguageSet();
    }

    @Override
    public void loadIVRResponseCache() {
        ubonaDAO.loadSongsCache();
    }

    @Override
    public void insertFeedbackLog(FeedBackRequest feedBackRequest, String queryString, String jsonRequest,
        String transID, String response) {
        ubonaDAO.insertFeedbackLog(feedBackRequest, queryString, jsonRequest, transID, response);
    }

    @Override
    public void saveRecommendedRespose(IVRRequest ivrRequest, String type, String transID, String response,String jsonResponse) {
        ubonaDAO.saveRecommendedRespose(ivrRequest, type, transID, response,jsonResponse);

    }

    @Override
    public void insertFailLog(CircleLang circlelang, String fetchSongs, int reqType, String msisdn, String txn_id) {
        ubonaDAO.insertFailLog(circlelang, fetchSongs, reqType, msisdn, txn_id);
    }

	@Override
	public String languageReco(LangRecoRequest langRecoRequest, String transID) {
		return iUbonaREHandler.languageReco(langRecoRequest, transID);
	}

	@Override
	public void saveLangRecoRespose(LangRecoRequest langRecoRequest,String transID, String jsonResponse) {
		ubonaDAO.saveRecommendedRespose(langRecoRequest, transID,jsonResponse);
		
	}
}
