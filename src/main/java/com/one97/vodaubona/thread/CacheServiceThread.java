/**
 * @author jugul.mishra
 * 
 *         Jul 4, 20146:44:23 PM
 */
package com.one97.vodaubona.thread;

import org.apache.log4j.Logger;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.beans.FinalResponseBean;
import com.one97.vodaubona.common.IConstants;
import com.one97.vodaubona.daoImpl.IUbonaDAO;
import com.one97.vodaubona.handler.IUbonaREHandler;

public class CacheServiceThread implements Runnable {
    private static Logger LOG = Logger.getLogger(CacheServiceThread.class);
    private CircleLang circlelang;
    private IUbonaREHandler iUbonaREHandler;
    private IUbonaDAO ubonaDAO;

    public CacheServiceThread() {

    }

    public CacheServiceThread(CircleLang circlelang, IUbonaREHandler iUbonaREHandler, IUbonaDAO ubonaDAO) {
        this.circlelang = circlelang;
        this.iUbonaREHandler = iUbonaREHandler;
        this.ubonaDAO = ubonaDAO;
    }

    @Override
    public void run() {
        try {
            FinalResponseBean finalResponseBean = iUbonaREHandler.fetchSongs(this.circlelang);
            if(finalResponseBean !=null)
            {
            	if (finalResponseBean.getIvrResponse().startsWith(IConstants.SUCCESS_MSG))
            		ubonaDAO.insertSongs(circlelang, finalResponseBean);
            	else
            		ubonaDAO.insertFailLog(circlelang, finalResponseBean.getIvrResponse(), 0, IConstants.DEFAULT_MSISDN, IConstants.DEFAULT_TXN_ID);
            }
        }
        catch (Exception ex) {
            LOG.error("Error Occured while caching data::: ", ex);
        }
    }
}
