/**
 * @author jugul.mishra
 * 
 *         Jul 10, 201412:41:00 PM
 */
package com.one97.vodaubona.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.one97.vodaubona.beans.CircleLang;
import com.one97.vodaubona.common.IConstants;
import com.one97.vodaubona.daoImpl.IUbonaDAO;
import com.one97.vodaubona.handler.IUbonaREHandler;

@Component
public class CacheService {
    @Autowired
    private IUbonaDAO ubonaDAO;
    @Value(value = "${chache.executorthread:2}")
    private Integer executorThread;
    @Autowired
    private IUbonaREHandler iCacheService;

    private static Logger LOG = Logger.getLogger(CacheService.class);

    public void startCacheService() {
        ExecutorService executor = null;
        try {
            if (IConstants.CIRCLE_LANG_SET.size() > 0) {
                executor = Executors.newFixedThreadPool(executorThread * Runtime.getRuntime().availableProcessors());
                for (CircleLang circlelang : IConstants.CIRCLE_LANG_SET) {
                    executor.execute(new CacheServiceThread(circlelang, iCacheService, ubonaDAO));
                }
                if (executor != null) {
                    executor.shutdown();
                    try {
                        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                        ubonaDAO.loadSongsCache();
                    }
                    catch (InterruptedException e) {
                        LOG.error("Error in Await termination ", e);
                    }
                }
                else {
                    LOG.info("Key Not found for Caching data");
                }
            }
        }
        catch (Exception e) {
            LOG.error("Error in Executor Service ", e);
        }

    }

}
