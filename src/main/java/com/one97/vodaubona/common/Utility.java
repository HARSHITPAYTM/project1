package com.one97.vodaubona.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.IVRRequest;
import com.one97.vodaubona.beans.LangRecoRequest;

public class Utility {

    private static Logger log = Logger.getLogger(Utility.class);

    
    public static String getFormattedFeedbackDate(String feedbackdate){
    	Date feedbackdt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(feedbackdate, new ParsePosition(0));
    	
        
    	String dt=feedbackdt==null?Utility.getYYYYMMDDDate():
    		new SimpleDateFormat("yyyyMMdd").format(feedbackdt);
    	
    	return dt;
    }
    
    public static int parseInt(String number) {
        try {
            return Integer.parseInt(number.trim());
        }
        catch (Exception e) {
            log.error("Number not parsable:::", e);
            return -1;
        }
    }

    public static long parseLong(String number) {
        try {
            return Long.parseLong(number.trim());
        }
        catch (Exception e) {
            log.error("Number not parsable:::", e);
            return -1;
        }
    }

    public static String getValidMsisdn(String msisdn, int desiredLength) {

        if (msisdn == null || msisdn.isEmpty() || !msisdn.matches("^\\d+$")) {
            return null;
        }

        if (desiredLength != 10 && desiredLength != 12 && desiredLength != 11) {
            log.error("Invalid msisdn length in config");
            return null;
        }
        if (msisdn.length() == 10) {
            if (msisdn.startsWith("0")) {
                log.error("Invalid msisdn.");
                return null;
            }
            if (desiredLength == 12) {
                msisdn = "91" + msisdn;
                return msisdn;
            }
            else if (desiredLength == 11) {
                msisdn = "0" + msisdn;
                return msisdn;
            }
        }
        else if (msisdn.length() == 12 && msisdn.startsWith("91")) {
            if (desiredLength == 11) {
                msisdn = "0" + msisdn.substring(2);
                return msisdn;
            }
            else if (desiredLength == 10) {
                msisdn = msisdn.substring(2);
                return msisdn;
            }
        }
        else if (msisdn.length() == 11 && msisdn.startsWith("0")) {
            if (desiredLength == 12) {
                msisdn = "91" + msisdn.substring(1);
                return msisdn;
            }
            else if (desiredLength == 10) {
                msisdn = msisdn.substring(1);
                return msisdn;
            }
        }
        else
            return null;
        return msisdn;
    }

    public static String getFormattedDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(new Date());
    }

    /**
     * This Method is used to called the url.
     * 
     * @param obRequestParamVO
     * @param timeOut
     * @return
     */

    public static String callHttpURL(String strUrl, int timeOut, String msisdn, boolean isJson, String feedBackRequest,
        String transID) {

        InputStreamReader obInputStreamReader = null;
        BufferedReader obBufferedReader = null;
        String strResponse = "";
        long startTime = 0;
        long timeTaken = 0;
        HttpURLConnection conn = null;
        try {
            strUrl = strUrl.replaceAll(" ", "%20");

            URL url = new URL(strUrl);

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            startTime = System.currentTimeMillis();
            if (isJson)
                sendJsonRequest(feedBackRequest, conn, transID);

            obInputStreamReader = new InputStreamReader(conn.getInputStream());

            obBufferedReader = new BufferedReader(obInputStreamReader);
            String sline = "";
            while ((sline = obBufferedReader.readLine()) != null)
                strResponse += sline;

        }
        catch (MalformedURLException e) {
            log.error("MalformedURLException while url calling(" + transID + "):::" + e.getMessage());
            strResponse = "ERROR:" + e.getMessage();
        }
        catch (IOException e) {
            log.error("IOException while url calling(" + transID + "):::" + e.getMessage());
            strResponse = "ERROR:" + e.getMessage();
        }
        catch (Exception e) {
            log.error("Exception while url calling(" + transID + "):::" + e.getMessage());
            strResponse = "ERROR:" + e.getMessage();
        }
        finally {
            try {
                if (obBufferedReader != null)
                    obBufferedReader.close();
                if (obInputStreamReader != null)
                    obInputStreamReader.close();
                if (conn != null)
                    conn.disconnect();
            }
            catch (Exception ex) {
                log.error("exception occur in closing BufferedReader or InputStreamReader  Resources  Error ("
                    + transID + "):", ex);
            }
            timeTaken = System.currentTimeMillis() - startTime;
            log.info("Status Url Response(" + transID + ") : MSISDN=" + msisdn + ", URL=" + strUrl + ", response="
                + strResponse + "::::Response Time  " + timeTaken);
        }
        return strResponse;
    }

    public static Object jsonToJava(String jsonresponse, Object object) {
        Object ivrResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            ivrResponse = mapper.readValue(jsonresponse, object.getClass());
        }
        catch (JsonGenerationException e) {
            log.error("Error while calling json parsing:::", e);
        }
        catch (JsonMappingException e) {
            log.error("Error while calling json parsing:::", e);
        }
        catch (IOException e) {
            log.error("Error while calling json parsing:::", e);
        }
        catch (Exception e) {
            log.error("Error while calling json parsing:::", e);
        }
        return ivrResponse;
    }

    public static boolean sendJsonRequest(String jsonbody, HttpURLConnection conn, String transID) {
        try {
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Match all request headers as seen in Chrome developer tools
            // when eavesdropping on client/server communication.
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
            log.info("JSON request(" + transID + "):::" + jsonbody);
            // mapper.writeValue(dataOutputStream, jsonbody);
            dataOutputStream.writeBytes(jsonbody);
            dataOutputStream.flush();
            dataOutputStream.close();
        }
        catch (Exception e) {
            log.error("Error while calling json request(" + transID + "):::" + e.getMessage());
            return false;
        }
        return true;
    }

    public static String javaToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(object);
        }
        catch (JsonGenerationException e) {
            log.error("Error while creating json object", e);
        }
        catch (JsonMappingException e) {
            log.error("Error while creating json object", e);
        }
        catch (IOException e) {
            log.error("Error while creating json object", e);
        }
        return jsonString;
    }

    public static String replaceURLParams(String fetchSongURL, IVRRequest ivrRequest) {
        return StringUtils.replaceEach(fetchSongURL, IConstants.PARAM_ARRAY, ivrRequest.getRequestParamArray());
    }

    public static String replaceURLParams(String fetchSongURL, CircleLang circleLang) {
        return fetchSongURL.replace(IConstants.LANGUAGE, circleLang.getLanguage()).replace(IConstants.CIRCLE,
            circleLang.getUbonaCircleName());
    }

    public static String getUbonaDate(String date) {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        String defaultDate = dateFormat1.format(new Date());
        try {
            return dateFormat2.format(dateFormat1.parse(date));
        }
        catch (Exception e) {
            try {
                return dateFormat2.format(dateFormat1.parse(defaultDate));
            }
            catch (ParseException e1) {
                return "0000-00-00T00:00:00Z";
            }
        }
    }

    /*public static String getYYYYMMDate() {
        DateFormat yyyymmDateFormate = new SimpleDateFormat("yyyyMMdd");
        try {
            return yyyymmDateFormate.format(new Date());
        }
        catch (Exception e) {
            return "000000";
        }
    }*/
	
	
	public static String getYYYYMMDDDate() {
        DateFormat yyyymmDateFormate = new SimpleDateFormat("yyyyMMdd");
        try {
            return yyyymmDateFormate.format(new Date());
        }
        catch (Exception e) {
            return "000000";
        }
    }

    public static int getHour(int reducedBy) {
        DateFormat hourDateFormat = new SimpleDateFormat("HH");
        try {
            return (parseInt(hourDateFormat.format(new Date())) - reducedBy);
        }
        catch (Exception e) {
            return -1;
        }
    }

    public static synchronized String getTransID() {
        return UUID.randomUUID().toString();

    }

    public static String getSongType(boolean isDefault) {
        if (isDefault)
            return "CACHE";
        else
            return "RE";
    }

    public static String upperCase(String str) {
        return StringUtils.upperCase(str);
    }

    public static String lowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

	public static String replaceLangRecoURLParams(String langRecoURL,LangRecoRequest langRecoRequest) {
		String url=langRecoURL;
		if(!StringUtils.isEmpty(url))
		{
			url=url.replace("<msisdn>", langRecoRequest.getMsisdn()).replace("<circle>", langRecoRequest.getCircle()).replace("<serviceid>", langRecoRequest.getServiceId()).replace("<txnid>", langRecoRequest.getTxnId());
		}
		return url;
	}

}
