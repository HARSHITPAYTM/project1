package com.one97.vodaubona.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FeedBackRequest;
import com.one97.vodaubona.beans.FeedbackCategory;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.LangRecoRequest;
import com.one97.vodaubona.common.IConstants;
import com.one97.vodaubona.common.Utility;
import com.one97.vodaubona.service.IServiceFacade;
import com.one97.vodaubona.thread.CacheService;

@Controller
public class RootController {

    private static final Logger log = Logger.getLogger(RootController.class);
    private static final Logger feedback_log = Logger.getLogger("feedback");
    @Value(value = "${msisdn.desiredLength}")
    private Integer desiredLength;
    @Autowired
    private IServiceFacade serviceFacade;
    @Autowired
    private CacheService cacheService;
    
    @Value(value = "${response.vxml.fetchsong.success:}")
    private String fetchSongsVxmlSuccessResponse;
    @Value(value = "${response.vxml.fetchsong.fail:}")
    private String fetchSongsVxmlFailResponse;
    @Value(value = "${response.ecma.fetchsong.success:}")
    private String fetchSongsEcmaSuccessResponse;
    @Value(value = "${response.ecma.fetchsong.fail:}")
    private String fetchSongsEcmaFailResponse;
    
    @Value(value = "${response.vxml.langreco.success:}")
    private String langRecoVxmlSuccessResponse;
    @Value(value = "${response.vxml.langreco.fail:}")
    private String langRecoVxmlFailResponse;
    @Value(value = "${response.ecma.langreco.success:}")
    private String langRecoEcmaSuccessResponse;
    @Value(value = "${response.ecma.langreco.fail:}")
    private String langRecoEcmaFailResponse;
    
    @Value(value = "${response.vxml.songFeedback.success:}")
    private String songFeedbackVxmlSuccessResponse;
    @Value(value = "${response.vxml.songFeedback.fail:}")
    private String songFeedbackVxmlFailResponse;
    @Value(value = "${response.ecma.songFeedback.success:}")
    private String songFeedbackEcmaSuccessResponse;
    @Value(value = "${response.ecma.songFeedback.fail:}")
    private String songFeedbackEcmaFailResponse;
    

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello() {
        return "home";
    }
    
//    http://localhost:8080/vodaubona-ct/fetchSongs?msisdn=1111111111&circle=DELHI&isUserSelectedLang=FALSE&language=HINDI&serviceId=1234&txnId=1801715102618280694571140621193&isSubscribed=FALSE&subscriptionClass=CRBT_CTSB39M&categorySongs=RE,15,CATEGORIES,15&noOfSubCategories=RE,5&responseType=
    @RequestMapping(value = { "/fetchSongs" }, method = { RequestMethod.GET }, produces = "TEXT/HTML")
    public void fetchSongs(@RequestParam(value = "msisdn", required = false) String msisdn,
        @RequestParam(value = "circle", required = false) String circle,
        @RequestParam(value = "isUserSelectedLang", required = false) String isUserSelectedLang,
        @RequestParam(value = "language", required = false) String language,
        @RequestParam(value = "serviceId", required = false) String serviceId,
        @RequestParam(value = "txnId", required = false) String txnId,
        @RequestParam(value = "isSubscribed", required = false) String isSubscribed,
        @RequestParam(value = "subscriptionClass", required = false) String subscriptionClass,
        @RequestParam(value = "responseType", required = false) String responseType,
        @RequestParam(value = "categorySongs", required = false) String categorySongs,
        @RequestParam(value = "noOfSubCategories", required = false) String noOfSubCategories,
        HttpServletResponse response) throws IOException {
        String transID = Utility.getTransID();
        String ivrresponse = "";
        String jsonResponse="";
        boolean isDefault = false;
        IVRRequest ivrRequest = null;
        FinalResponseBean finalResponseBean=null;
        String ubApiResponse="";
        boolean isSuccess=false;
        String finalResponse="";
        try {
            log.info("Fetch Songs Request("
                + transID
                + ")::"
                + new StringBuilder().append("IVRRequest [msisdn=").append(msisdn).append(", circle=").append(circle)
                    .append(", isUserSelectedLang=").append(isUserSelectedLang).append(", language=").append(language)
                    .append(", serviceId=").append(serviceId).append(", txnId=").append(txnId)
                    .append(", isSubscribed=").append(isSubscribed).append(", subscriptionClass=")
                    .append(subscriptionClass).append(", categorySongs=")
                    .append(categorySongs).append(", noOfSubCategories=").append(noOfSubCategories)
                    .append("]").toString());

            ivrRequest = new IVRRequest(msisdn, StringUtils.upperCase(circle), isUserSelectedLang,
                StringUtils.upperCase(language), serviceId, txnId, isSubscribed, subscriptionClass,categorySongs,noOfSubCategories);

            finalResponseBean = serviceFacade.fetchSongs(ivrRequest, transID);
            if(finalResponseBean !=null)
            {
            	ivrresponse=finalResponseBean.getIvrResponse();
            	jsonResponse=finalResponseBean.getJsonResponse();
            }

            if (StringUtils.isEmpty(ivrresponse) || !ivrresponse.toLowerCase().startsWith(IConstants.SUCCESS_MSG)) {
                serviceFacade.insertFailLog(
                    new CircleLang(ivrRequest.getCircle(), ivrRequest.getCircle(), ivrRequest.getCircle(), ivrRequest
                        .getLanguage()), ivrresponse, 1, msisdn, txnId);
                log.info("Fetch Songs From Cache(" + transID + ")");
                finalResponseBean = IConstants.SONGS_CACHE_MAP.get(ivrRequest.getCircle() + ivrRequest.getLanguage());
                if(finalResponseBean !=null)
                {
                	ivrresponse=finalResponseBean.getIvrResponse();
                	jsonResponse=finalResponseBean.getJsonResponse();

                	if (!StringUtils.isEmpty(ivrresponse))
                	{
                		ivrresponse = ivrresponse.replace(IConstants.SUCCESS_MSG, "SUCCESSDEF");
                		ubApiResponse="SUCCESSDEF";
                		isSuccess=true;
                	}
                }
                isDefault = true;
            }
            else
            {
                ivrresponse = ivrresponse.replace(IConstants.SUCCESS_MSG, "SUCCESSURE");
                ubApiResponse="SUCCESSURE";
                isSuccess=true;
            }

            if (StringUtils.isEmpty(ivrresponse))
            {
                ivrresponse = IConstants.FAIL_MSG;
                isSuccess=false;
            }
            
            if (StringUtils.isEmpty(jsonResponse))
            {
            	jsonResponse = "";
            }

        }
        catch (Exception e) {
            log.error("Exception while fetching songs(" + transID + ")::" + e.getMessage());
            ivrresponse = IConstants.FAIL_MSG;
            isSuccess=false;
        }
        finally {
            serviceFacade.saveRecommendedRespose(ivrRequest, Utility.getSongType(isDefault), transID, ivrresponse,jsonResponse);
        }
        log.info("Fetch Songs Response(" + transID + ")::" + ivrresponse);
        
        
        if(responseType==null || responseType.isEmpty() || responseType.equalsIgnoreCase("text"))
        {
        	finalResponse=ivrresponse;
        	response.setContentType("text/plain charset=ISO-8859-1");
        }
        else if(responseType.equalsIgnoreCase("ecma"))
        {
        	finalResponse=getFinalECMAResponse(isSuccess,ivrresponse,jsonResponse,ubApiResponse);
        	response.setContentType("application/ecmascript; charset=ISO-8859-1");
        }
        else if(responseType.equalsIgnoreCase("vxml"))
        {
        	finalResponse=getFinalVXMLResponse(isSuccess,ivrresponse,jsonResponse,ubApiResponse);
        	response.setContentType("application/xml; charset=ISO-8859-1");
        }
        else
        {
        	finalResponse=ivrresponse;
        	response.setContentType("text/plain charset=ISO-8859-1");
        }
        log.info("Final Response : "+finalResponse);
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(finalResponse);
    }

    
    
    @RequestMapping(value = { "/languageReco" }, method = { RequestMethod.GET }, produces = "TEXT/HTML")
    public void languageReco(@RequestParam(value = "msisdn", required = false) String msisdn,
        @RequestParam(value = "circle", required = false) String circle,
        @RequestParam(value = "serviceId", required = false) String serviceId,
        @RequestParam(value = "txnId", required = false) String txnId,
        @RequestParam(value = "responseType", required = false) String responseType,HttpServletResponse response) throws IOException {
        String transID = Utility.getTransID();
        String jsonResponse="";
        LangRecoRequest langRecoRequest=null;
        boolean isSuccess=false;
        String finalResponse="";
        try {
            log.info("languageReco Request("
                + transID
                + ")::"
                + new StringBuilder().append("IVRRequest [msisdn=").append(msisdn).append(", circle=").append(circle)
                    .append(", serviceId=").append(serviceId).append(", txnId=").append(txnId).append("]").toString());
            
            langRecoRequest=new LangRecoRequest(msisdn, StringUtils.upperCase(circle), serviceId, txnId);

            jsonResponse = serviceFacade.languageReco(langRecoRequest, transID);

            if (StringUtils.isEmpty(jsonResponse) || jsonResponse.startsWith(IConstants.FAIL_MSG) || jsonResponse.startsWith("ERROR")) {
                 isSuccess=false;
            }
            else
            {
                isSuccess=true;
            }

            if (StringUtils.isEmpty(jsonResponse))
            {
            	jsonResponse = IConstants.FAIL_MSG;
                isSuccess=false;
            }
            
        }
        catch (Exception e) {
            log.error("Exception in language reco (" + transID + ")::" + e.getMessage());
            jsonResponse = IConstants.FAIL_MSG;
            isSuccess=false;
        }
        finally {
            serviceFacade.saveLangRecoRespose(langRecoRequest, transID,jsonResponse);
        }
        log.info("language reco Response(" + transID + ")::" + jsonResponse);
        
        
        if(responseType==null || responseType.isEmpty() || responseType.equalsIgnoreCase("text"))
        {
        	finalResponse=jsonResponse;
        	response.setContentType("text/plain charset=ISO-8859-1");
        }
        else if(responseType.equalsIgnoreCase("ecma"))
        {
        	finalResponse=getFinalECMAResponseForLangReco(isSuccess,jsonResponse);
        	response.setContentType("application/ecmascript; charset=ISO-8859-1");
        }
        else if(responseType.equalsIgnoreCase("vxml"))
        {
        	finalResponse=getFinalVXMLResponseForLangReco(isSuccess,jsonResponse);
        	response.setContentType("application/xml; charset=ISO-8859-1");
        }
        else
        {
        	finalResponse=jsonResponse;
        	response.setContentType("text/plain charset=ISO-8859-1");
        }
        log.info("Final Response : "+finalResponse);
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(finalResponse);
    }
    
    
    
    
    
    @RequestMapping(value = { "/songsFeedback" }, method = { RequestMethod.GET, RequestMethod.POST }, produces = "TEXT/HTML")
    public void songsFeedback(@RequestParam(value = "queryString", required = false) String queryString,
    		@RequestParam(value = "responseType", required = false) String responseType,HttpServletResponse resp)throws IOException {
        String response = "";
        String finalResponse="";
        boolean isSuccess=false;
        String transID = Utility.getTransID();
        try {
            feedback_log.info("queryString(" + transID + ")=" + queryString);
            if (!StringUtils.isEmpty(queryString))
            {
            FeedBackRequest feedbackReq = createFeedbackRequest(queryString, transID);
            feedback_log.info("Feedback Request(" + transID + "):::" + feedbackReq);
            response = serviceFacade.recoFeedBack(feedbackReq, transID, queryString);
            isSuccess=true;
            
            if (response.startsWith(IConstants.FAIL_MSG) || response.startsWith("ERROR"))
            {
            	if(response.startsWith(IConstants.FAIL_MSG))
                    response = response.split(IConstants.HASH_SEPARATOR)[0];
      
                isSuccess=false;
            }
            }
            else
            	response=IConstants.FAIL_MSG;
        }
        catch (Exception e) {
            feedback_log.error("Exception while songsFeedback(" + transID + ")::", e);
            response = "Fail::" + IConstants.TECH_ERROR;
            isSuccess=false;
        }
        if(responseType==null || responseType.isEmpty() || responseType.equalsIgnoreCase("text"))
        {
        	finalResponse=response;
        	resp.setContentType("text/plain charset=ISO-8859-1");
        }
        else if(responseType.equalsIgnoreCase("ecma"))
        {
        	finalResponse=getFinalECMAResponseForSongFeedback(isSuccess,response);
        	resp.setContentType("application/ecmascript; charset=ISO-8859-1");
        }
        else if(responseType.equalsIgnoreCase("vxml"))
        {
        	finalResponse=getFinalVXMLResponseForSongFeedback(isSuccess,response);
        	resp.setContentType("application/xml; charset=ISO-8859-1");
        }
        else
        {
        	finalResponse=response;
        	resp.setContentType("text/plain charset=ISO-8859-1");
        }
        feedback_log.info("Final Response : "+finalResponse);
        resp.setHeader("Cache-Control", "no-cache");
        resp.getWriter().write(finalResponse);
    }

    private FeedBackRequest createFeedbackRequest(String queryString, String transID) {
        FeedBackRequest request = new FeedBackRequest();
        List<FeedbackCategory> categories = new ArrayList<FeedbackCategory>();
        try {
            if (!StringUtils.isEmpty(queryString)) {

                String[] paramArray = queryString.split(IConstants.COMMA_SEPARATOR);
                request.setFeedbackTime(stringByIndex(paramArray, 0));
                request.setMsisdn(stringByIndex(paramArray, 1));
                request.setCircle(stringByIndex(paramArray, 2));
                request.setIsUserSelectedLang(stringByIndex(paramArray, 3));
                request.setLanguage(stringByIndex(paramArray, 4));
                request.setServiceId(stringByIndex(paramArray, 5));
                request.setTxnId(stringByIndex(paramArray, 6));
                request.setIsSubscribed(stringByIndex(paramArray, 7));
                request.setSubscriptionClass(stringByIndex(paramArray, 8));
                request.setTotalSOU(stringByIndex(paramArray, 9));
                if (stringByIndex(paramArray, 10) != null)
                    request.setUserTrail(stringByIndex(paramArray, 10).replace(IConstants.LINE_SEPARATOR,
                        IConstants.COMMA_SEPARATOR));
                try {
                    if (!StringUtils.isEmpty(stringByIndex(paramArray, 11))) {

                        String[] catArray = stringByIndex(paramArray, 11).split("\\" + IConstants.TILDA_SEPARATOR);
                        FeedbackCategory feedbackCategory = null;
                        for (String category : catArray) {
                            if (!StringUtils.isEmpty(category)) {
                                String[] cat = category.split("\\" + IConstants.ASTRIX);
                                feedbackCategory = new FeedbackCategory();
                                feedbackCategory.setCategoryId(stringByIndex(cat, 0));
                                feedbackCategory.setStartSOU(stringByIndex(cat, 1));
                                feedbackCategory.setEndSOU(stringByIndex(cat, 2));
                                String songSelected = stringByIndex(cat, 3);
                                if (!StringUtils.isEmpty(songSelected)) {
                                    feedbackCategory.setSongSelected(songSelected);
                                }
                                String songsPlayed = stringByIndex(cat, 4);
                                List<String> songsList = new ArrayList<String>();
                                if (songsPlayed != null) {
                                    String[] split = songsPlayed.split("\\|");
                                    if (split != null)
                                        songsList.addAll(Arrays.asList(split));
                                }
                                feedbackCategory.setSongsPlayed(songsList);
                                categories.add(feedbackCategory);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    feedback_log.error("Transaction ID(" + transID + ")::" + queryString + "=" + e.getMessage());
                }
                request.setSongPlayed(categories);
            }
        }
        catch (Exception e) {
            feedback_log.error("Error occured(" + transID + ")::", e);
        }
        return request;
    }

    @PostConstruct
    public void loadAllCachedMap() {
        // Load IConstants.CIRCLE_LANG_SET
        serviceFacade.loadCircleLanguageSet();
        // Load IConstants.SONGS_CACHE_MAP
        serviceFacade.loadIVRResponseCache();

    }

    @Scheduled(cron = "${cron.expression}")
    public void loadDefaultSongs() {
        cacheService.startCacheService();
    }

    private String stringByIndex(String[] array, int index) {
        try {
            if (!StringUtils.isEmpty(array[index]))
                return array[index];
        }
        catch (Exception e) {
            log.error("Error in array index at:::" + index + "::::" + e.getMessage());
        }
        return null;
    }
    
    
    private String getFinalVXMLResponseForLangReco(boolean isSuccess,String jsonResponse) 
    {
		String finalResponse="";
		if(isSuccess)
		{
			finalResponse=langRecoVxmlSuccessResponse;
			finalResponse=finalResponse.replace("<param1>",jsonResponse);
		}
		else
		{
			finalResponse=langRecoVxmlFailResponse;
			finalResponse=finalResponse.replace("<param1>",jsonResponse);
		}
		return finalResponse;
	}

	private String getFinalECMAResponseForLangReco(boolean isSuccess,String jsonResponse) 
	{
		String finalResponse="";
		if(isSuccess)
		{
			finalResponse=langRecoEcmaSuccessResponse;
			finalResponse=finalResponse.replace("<param1>",jsonResponse);
		}
		else
		{
			finalResponse=langRecoEcmaFailResponse;
			finalResponse=finalResponse.replace("<param1>",jsonResponse);
		}
		return finalResponse;
	}
	
	private String getFinalVXMLResponse(boolean isSuccess,String ivrresponse,String jsonResponse,String ubApiResponse) 
    {
		String finalResponse="";
		if(isSuccess)
		{
			finalResponse=fetchSongsVxmlSuccessResponse;
			finalResponse=finalResponse.replace("<param1>",ubApiResponse).replace("<param2>",jsonResponse);
		}
		else
		{
			finalResponse=fetchSongsVxmlFailResponse;
			finalResponse=finalResponse.replace("<param1>",ivrresponse);
		}
		return finalResponse;
	}

	private String getFinalECMAResponse(boolean isSuccess,String ivrresponse,String jsonResponse,String ubApiResponse) 
	{
		String finalResponse="";
		if(isSuccess)
		{
			finalResponse=fetchSongsEcmaSuccessResponse;
			finalResponse=finalResponse.replace("<param1>",ubApiResponse).replace("<param2>",jsonResponse);
		}
		else
		{
			finalResponse=fetchSongsEcmaFailResponse;
			finalResponse=finalResponse.replace("<param1>",ivrresponse);
		}
		return finalResponse;
	}
	
	private String getFinalVXMLResponseForSongFeedback(boolean isSuccess,String response) 
    {
		String finalResponse="";
		if(isSuccess)
		{
			finalResponse=songFeedbackVxmlSuccessResponse;
			finalResponse=finalResponse.replace("<param1>",response);
		}
		else
		{
			finalResponse=songFeedbackVxmlFailResponse;
			finalResponse=finalResponse.replace("<param1>",response);
		}
		return finalResponse;
	}

	private String getFinalECMAResponseForSongFeedback(boolean isSuccess,String response) 
	{
		String finalResponse="";
		if(isSuccess)
		{
			finalResponse=songFeedbackEcmaSuccessResponse;
			finalResponse=finalResponse.replace("<param1>",response);
		}
		else
		{
			finalResponse=songFeedbackEcmaFailResponse;
			finalResponse=finalResponse.replace("<param1>",response);
		}
		return finalResponse;
	}
}
