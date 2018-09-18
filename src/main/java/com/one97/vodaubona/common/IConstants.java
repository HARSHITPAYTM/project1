package com.one97.vodaubona.common;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FinalResponseBean;

public interface IConstants {

    String SUCCESS_CODE = "0";
    String ONE = "1";
    String ZERO = "0";

    String SUCCESS_MSG = "success";
    String FAIL_MSG = "fail";

    /**** Start Songs URL Constants ***/
    String MSISDN = "<msisdn>";
    String CIRCLE = "<circle>";
    String ISUSERSELECTEDLANG = "<isuserlang>";
    String LANGUAGE = "<lang>";
    String SERVICEID = "<serviceid>";
    String TXNID = "<txnid>";
    String ISSUBSCRIBED = "<issubs>";
    String SUBSCRIPTIONCLASS = "<subclass>";
    String CATEGORYSONGS  = "<categorysongs>";
    String NOOFSUBCATEGORIES = "<noofsubcategories>";
    /**** End Songs URL Constants ***/
    String TECH_ERROR = "Technical Error!";
    String[] PARAM_ARRAY = { MSISDN, CIRCLE, ISUSERSELECTEDLANG, LANGUAGE, SERVICEID, TXNID, ISSUBSCRIBED,
        SUBSCRIPTIONCLASS, CATEGORYSONGS, NOOFSUBCATEGORIES};

    String LINE_SEPARATOR = "|";
    String HASH_SEPARATOR = "#";
    String VALUE_ASSIGN_SEPARATOR = ":";
    String COMMA_SEPARATOR = ",";
    String SEMICOLON_SEPARATOR = ";";
    String ASTRIX = "*";
    String CATEGORY = "cat";
    String SUB_CATEGORY = "subcat";
    String SUB_CATEGORY_FLAG = "subcatflag";
    String SONGS = "songs";
    String MAIN = "main";
    String DEFAULT = "DEFAULT";
    String DEFAULT_MSISDN = "1111111111";
    String DEFAULT_TXN_ID = "1111111111";

    Set<CircleLang> CIRCLE_LANG_SET = new HashSet<CircleLang>();
    ConcurrentMap<String, FinalResponseBean> SONGS_CACHE_MAP = new ConcurrentHashMap<String, FinalResponseBean>();
    String TILDA_SEPARATOR = "~";
    String CACHE_REQ = "CACHE";
    String SONGS_REQ = "SONGS";
    String FEEDBACK_REQ = "FEEDBACK";
}
