package com.one97.vodaubona.daoImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.mchange.v2.sql.SqlUtils;
import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FeedBackRequest;
import com.one97.vodaubona.beans.FeedbackCategory;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.LangRecoRequest;
import com.one97.vodaubona.common.DbConnection;
import com.one97.vodaubona.common.IConstants;
import com.one97.vodaubona.common.Utility;

@Repository
public class UbonaDAOImpl extends DbConnection implements IUbonaDAO {

    private Logger log = Logger.getLogger(UbonaDAOImpl.class);
    private static final Logger feedback_log = Logger.getLogger("feedback");
    private static final Logger feedback_log_db = Logger.getLogger("feedback_db");
    

    @Override
    public void loadCircleLanguageSet() {
        try {
            String sql = "SELECT circle_code,one97_circle_name,ubona_circle_name,language_code FROM tbl_circlemaster, tbl_language";
            SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql);
            while (rs.next()) {
                IConstants.CIRCLE_LANG_SET.add(new CircleLang(rs.getString("circle_code"), rs
                    .getString("one97_circle_name"), rs.getString("ubona_circle_name"), rs.getString("language_code")));
            }
        }
        catch (Exception e) {
            if (e.getMessage().contains("doesn't exist"))
                log.error("Error while loading circle language::" + e.getMessage());
            else
                log.error("Error while loading circle language::", e);
        }
    }

    @Override
    public void loadSongsCache() {
        String sql = "SELECT circle_name, `language_code`, ivr_response,json_response FROM `tbl_ivr_response`";
        FinalResponseBean finalResponseBean=null;
        try {
            SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql);
            while (rs.next()) {
            	finalResponseBean=new FinalResponseBean();
            	finalResponseBean.setIvrResponse(rs.getString("ivr_response"));
            	finalResponseBean.setJsonResponse(rs.getString("json_response"));
                IConstants.SONGS_CACHE_MAP.put(
                    StringUtils.upperCase(rs.getString("circle_name"))
                        + StringUtils.upperCase(rs.getString("language_code")), finalResponseBean);
            }
        }
        catch (Exception e) {
            log.error("Error while loading circle language::", e);
        }

        log.debug("Updated Cache Map::" + IConstants.SONGS_CACHE_MAP);
    }

    @Override
    public void insertSongs(CircleLang circlelang, FinalResponseBean finalResponseBean) {
        String sql = "INSERT INTO `tbl_ivr_response` (circle_name, language_code, ivr_response,json_response) VALUES (?,?,?,?)"
            + " ON DUPLICATE KEY UPDATE ivr_response = ?,json_response=?, insert_date=now()";
        try {
            getJdbcTemplate().update(sql, circlelang.getUbonaCircleName(), circlelang.getLanguage(), finalResponseBean.getIvrResponse(),finalResponseBean.getJsonResponse(),
            		finalResponseBean.getIvrResponse(),finalResponseBean.getJsonResponse());
        }
        catch (Exception e) {
            log.error("Error while insert::", e);
        }
    }

    @Override
    public void insertFailLog(CircleLang circlelang, String response, int reqType, String msisdn, String txn_id) {
        String sql = "INSERT INTO `tbl_fail_response` (msisdn,txn_id,circle_name, language_code, ivr_response,req_type) VALUES (?,?,?,?,?,?)";
        try {
            log.info("Request Data insert in tbl_fail_repsone");
            getJdbcTemplate().update(sql, msisdn, txn_id, circlelang.getUbonaCircleName(), circlelang.getLanguage(),
                response, getRequestType(reqType));
        }
        catch (Exception e) {
            log.error("Error while insert::", e);
        }
    }

    private String getRequestType(int reqType) {
        String requestType = "";
        switch (reqType) {
            case 0:
                requestType = IConstants.CACHE_REQ;
                break;
            case 1:
                requestType = IConstants.SONGS_REQ;
                break;
            case 2:
                requestType = IConstants.FEEDBACK_REQ;
                break;

        }
        return requestType;
    }

    @Override
    public void insertFeedbackLog(FeedBackRequest feedBackRequest, String queryString, String jsonRequest,
        String transID, String response) {
    	
    	boolean errorflag=false;
    	
    	String dt=Utility.getFormattedFeedbackDate(feedBackRequest.getFeedbackTime());
    	
        String sql = "INSERT INTO `tbl_feedbacklog"
            //+ Utility.getYYYYMMDDDate()
        	+dt
            + "` (msisdn,txn_id,circle_name,language_code,query_string,feedback_time,songselected,feedback_request,response) "
            + "VALUES('" + feedBackRequest.getMsisdn() + "','" + feedBackRequest.getTxnId() + "','"
            + StringUtils.upperCase(feedBackRequest.getCircle()) + "','"
            + StringUtils.upperCase(feedBackRequest.getLanguage()) + "','"
            + SqlUtils.escapeBadSqlPatternChars(queryString) + "','" + feedBackRequest.getFeedbackTime() + "','"
            + getSongsSelected(feedBackRequest) + "','" + jsonRequest + "','" + response + "');";

        String creatSql = "Create Table `tbl_feedbacklog" + dt + "` like `tbl_feedbacklog_base`";
        try {
            feedback_log.info("insert logs in feedbacklog table(" + transID + ") SQL=" + sql);
            getJdbcTemplate().update(sql);
        }
        catch (Exception e) {
        	
            if (e.getMessage().contains("doesn't exist")) {
            	try{
	                getJdbcTemplate().execute(creatSql);
	                getJdbcTemplate().update(sql);
            	}
            	catch(Exception e1){
            		errorflag=true;
            	}
	        }
            else{
                feedback_log.error("Error while insert(" + transID + ")::", e);
                errorflag=true;

            }
        }
        
        finally{
        	if(errorflag){
        		feedback_log_db.info(sql);
        	}
        }
    }

    @Override
    public void saveRecommendedRespose(IVRRequest ivrRequest, String type, String transID, String response,String jsonResponse) {
        String sql = "INSERT INTO `tbl_recommendation" + Utility.getYYYYMMDDDate()
            + "` (msisdn,circle_name,isUserSelectedLang,language_code,serviceId,txn_id,isSubscribed,"
            + "subscriptionClass,response,json_response,response_type, response_time)VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
        String creatSql = "Create Table `tbl_recommendation" + Utility.getYYYYMMDDDate()
            + "` like `tbl_recommendation_base`";
        try {
            log.info("insert logs in recommendation table(" + transID + ")=" + ivrRequest + ",type=" + type
                + ",response=" + response);
            getJdbcTemplate().update(sql, ivrRequest.getMsisdn(), StringUtils.upperCase(ivrRequest.getCircle()),
                ivrRequest.getIsUserSelectedLang(), StringUtils.upperCase(ivrRequest.getLanguage()),
                ivrRequest.getServiceId(), ivrRequest.getTxnId(), ivrRequest.getIsSubscribed(),
                ivrRequest.getSubscriptionClass(), response, jsonResponse, type, ivrRequest.getTimeTaken());
        }
        catch (Exception e) {
            if (e.getMessage().contains("doesn't exist")) {
                getJdbcTemplate().execute(creatSql);
                getJdbcTemplate().update(sql, ivrRequest.getMsisdn(), StringUtils.upperCase(ivrRequest.getCircle()),
                    ivrRequest.getIsUserSelectedLang(), StringUtils.upperCase(ivrRequest.getLanguage()),
                    ivrRequest.getServiceId(), ivrRequest.getTxnId(), ivrRequest.getIsSubscribed(),
                    ivrRequest.getSubscriptionClass(), response, jsonResponse, type, ivrRequest.getTimeTaken());
            }
            else
                log.error("Error while insert(" + transID + ")::", e);
        }
    }

    private String getSongsSelected(FeedBackRequest feedBackRequest) {
        String songs = "";
        if (feedBackRequest != null) {
            List<FeedbackCategory> songPlayed = feedBackRequest.getSongPlayed();
            if (songPlayed != null)
                for (FeedbackCategory feedbackCategory : songPlayed)
                    songs += feedbackCategory.getSongSelected() == null ? ""
                        : (feedbackCategory.getSongSelected() + "|");
        }
        return songs;
    }

	@Override
	public void saveRecommendedRespose(LangRecoRequest langRecoRequest,	String transID, String jsonResponse) {
		String sql = "insert into tbl_lang_reco_" + Utility.getYYYYMMDDDate()
	            + " (msisdn,circle,serviceId,txn_id,response,response_time,insert_date) VALUES(?,?,?,?,?,?,now());";
	        String creatSql = "Create Table tbl_lang_reco_" + Utility.getYYYYMMDDDate()
	            + " like tbl_lang_reco_base";
	        try {
	            log.info("insert logs in tbl_lang_reco_ table(" + transID + ")=" + langRecoRequest +  ",jsonResponse=" + jsonResponse);
	            getJdbcTemplate().update(sql, langRecoRequest.getMsisdn(), StringUtils.upperCase(langRecoRequest.getCircle()),
	            		langRecoRequest.getServiceId(), langRecoRequest.getTxnId(), jsonResponse,langRecoRequest.getResponseTime());
	        }
	        catch (Exception e) {
	            if (e.getMessage().contains("doesn't exist")) {
	                getJdbcTemplate().execute(creatSql);
	                getJdbcTemplate().update(sql, langRecoRequest.getMsisdn(), StringUtils.upperCase(langRecoRequest.getCircle()),
		            		langRecoRequest.getServiceId(), langRecoRequest.getTxnId(), jsonResponse,langRecoRequest.getResponseTime());
	            }
	            else
	                log.error("Error while insert(" + transID + ")::", e);
	        }
		
	}
}
