package com.one97.vodaubona.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.one97.vodaubona.beans.Category;
import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FeedBackRequest;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.IVRResponse;
import com.one97.vodaubona.beans.LangRecoRequest;
import com.one97.vodaubona.common.IConstants;
import com.one97.vodaubona.common.Utility;
import com.one97.vodaubona.daoImpl.IUbonaDAO;

@Component
public class UbonaREHandlerImpl implements IUbonaREHandler {

    private final Logger LOG = Logger.getLogger(UbonaREHandlerImpl.class);
    private static final Logger feedback_log = Logger.getLogger("feedback");

    @Value(value = "${vodafone.ubona.fetchsongurl}")
    private String fetchSongURL;
    @Value(value = "${default.ubona.fetchsongurl}")
    private String defaultFetchSongURL;
    @Value(value = "${vodafone.ubona.feedbackurl}")
    private String feedBackURL;
    @Value(value = "${vodafone.ubona.langrecourl}")
    private String langRecoURL;
    @Value(value = "${timeout:3000}")
    private Integer timeout;
    @Autowired
    private IUbonaDAO ubonaDAO;

    @Override
    public FinalResponseBean fetchSongs(CircleLang circleLang) {
        String ivrResponse = null;
        FinalResponseBean finalResponseBean=new FinalResponseBean();
        try {
            String response = Utility.callHttpURL(Utility.replaceURLParams(defaultFetchSongURL, circleLang), timeout,
                IConstants.DEFAULT, false, null, "ONE97-111111");
            finalResponseBean.setJsonResponse(response);
            IVRResponse ivrObj = null;
            if (!response.startsWith("ERROR")) {
                ivrObj = (IVRResponse) Utility.jsonToJava(response, new IVRResponse());
                ivrResponse = parseFetchSongsResponse(ivrObj);
            }
            else
                ivrResponse = response;
        }
        catch (Exception e) {
            LOG.info("Exception:::", e);
            ivrResponse = IConstants.FAIL_MSG + "::" + e.getMessage();
        }
        finalResponseBean.setIvrResponse(ivrResponse);
        return finalResponseBean;
    }

    @Override
    public FinalResponseBean fetchSongs(IVRRequest ivrRequest, String transID) {
        String ivrResponse = null;
        FinalResponseBean finalResponseBean=new FinalResponseBean();
        long startTime = System.currentTimeMillis();
        try {
            String response = Utility.callHttpURL(Utility.replaceURLParams(fetchSongURL, ivrRequest), timeout,
                ivrRequest.getMsisdn(), false, null, transID);
            ivrRequest.setTimeTaken(System.currentTimeMillis() - startTime);
            finalResponseBean.setJsonResponse(response);
            IVRResponse ivrObj = null;
            if (!response.startsWith("ERROR")) {
                ivrObj = (IVRResponse) Utility.jsonToJava(response, new IVRResponse());
                ivrResponse = parseFetchSongsResponse(ivrObj);
            }
            else {
                ivrResponse = response;
            }
            LOG.info("Response(" + transID + "):::" + ivrObj);
        }
        catch (Exception e) {
            LOG.info("Exception(" + transID + "):::", e);
            ivrResponse = IConstants.FAIL_MSG + "::" + e.getMessage();
        }

        finalResponseBean.setIvrResponse(ivrResponse);
        return finalResponseBean;
    }

    private String parseFetchSongsResponse(IVRResponse ivrResponse) {

        if (ivrResponse != null && ivrResponse.getResponseCode().equalsIgnoreCase(IConstants.SUCCESS_CODE)) {
            StringBuilder builder = new StringBuilder();
            builder.append(IConstants.SUCCESS_MSG);
            builder.append(IConstants.LINE_SEPARATOR + "\n");
            builder.append(IConstants.CATEGORY);
            builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
            Category category = ivrResponse.getCategories();
            if (category != null) {
                builder.append(IConstants.MAIN);
                builder.append(IConstants.SEMICOLON_SEPARATOR);
                builder.append(IConstants.SUB_CATEGORY);
                builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
                builder.append(category.getCategoryId());
                builder.append(IConstants.SEMICOLON_SEPARATOR);
                builder.append(IConstants.SUB_CATEGORY_FLAG);
                builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
                builder.append(IConstants.ZERO);
                builder.append(IConstants.SEMICOLON_SEPARATOR);
                builder.append(IConstants.SONGS);
                builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
                builder.append(StringUtils.collectionToCommaDelimitedString(category.getCategorySongs()));
                builder.append(IConstants.LINE_SEPARATOR + "\n");
                List<Category> subCategories = category.getSubCategories();
                if (subCategories != null && subCategories.size() > 0) {
                    for (Category subCategory : subCategories)
                        traverseSubCategories(subCategory, builder, category.getCategoryId());
                }
            }
            else
                builder.append(0);

            return builder.toString();
        }
        else
            return IConstants.FAIL_MSG + ivrResponse == null ? "" : IConstants.HASH_SEPARATOR + ivrResponse.toString();

    }

    private void traverseSubCategories(Category subCategory, StringBuilder builder, String parentCategory) {

        List<Category> subCategories = subCategory.getSubCategories();
        builder.append(IConstants.CATEGORY);
        builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
        builder.append(parentCategory);
        builder.append(IConstants.SEMICOLON_SEPARATOR);
        builder.append(IConstants.SUB_CATEGORY);
        builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
        builder.append(subCategory.getCategoryId());
        builder.append(IConstants.SEMICOLON_SEPARATOR);
        builder.append(IConstants.SUB_CATEGORY_FLAG);
        builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
        if (subCategories != null && subCategories.size() > 0)
            builder.append(IConstants.ONE);
        else
            builder.append(IConstants.ZERO);
        builder.append(IConstants.SEMICOLON_SEPARATOR);
        builder.append(IConstants.SONGS);
        builder.append(IConstants.VALUE_ASSIGN_SEPARATOR);
        builder.append(StringUtils.collectionToCommaDelimitedString(subCategory.getCategorySongs()));
        builder.append(IConstants.LINE_SEPARATOR + "\n");
        if (subCategories != null && subCategories.size() > 0) {
            for (Category subSubCategory : subCategories)
                traverseSubCategories(subSubCategory, builder, subCategory.getCategoryId());

        }
    }

    @Override
    public String recoFeedBack(final FeedBackRequest feedBackRequest, final String transID, final String queryString) {

        if (feedBackRequest != null) {
            String feedbackTime = feedBackRequest.getFeedbackTime();
            feedBackRequest.setFeedbackTime(Utility.getUbonaDate(feedbackTime));
            final String jsonRequest = Utility.javaToJson(feedBackRequest);
            feedback_log.info("JSON Feedback Request(" + transID + "):::" + jsonRequest);
            feedBackRequest.setFeedbackTime(feedbackTime);
            final String response = Utility.callHttpURL(feedBackURL, timeout, feedBackRequest.getMsisdn(), true,jsonRequest, transID);
          

            if (response.contains("ERROR"))
                ubonaDAO.insertFailLog(new CircleLang(feedBackRequest.getCircle(), feedBackRequest.getCircle(),
                    feedBackRequest.getCircle(), feedBackRequest.getLanguage()), response, 2, feedBackRequest
                    .getMsisdn(), feedBackRequest.getTxnId());
            ubonaDAO.insertFeedbackLog(feedBackRequest, queryString, jsonRequest, transID, response);

            feedback_log.info("response(" + transID + "):::" + response);
            return response;
        }

        return null;
    }

	@Override
	public String languageReco(LangRecoRequest langRecoRequest, String transID) {
		String response = null;
		long startTime = System.currentTimeMillis();
        try {
            response = Utility.callHttpURL(Utility.replaceLangRecoURLParams(langRecoURL, langRecoRequest), timeout,
            		langRecoRequest.getMsisdn(), false, null, transID);
        }
        catch (Exception e) {
            LOG.info("Exception:::", e);
            response = IConstants.FAIL_MSG + "::" + e.getMessage();
        }
        langRecoRequest.setResponseTime(System.currentTimeMillis()-startTime);
        return response;
	}

}
