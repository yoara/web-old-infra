package org.yoara.framework.component.web.session;

import org.yoara.framework.component.web.cache.BridgeCache;
import org.yoara.framework.component.web.cache.CacheConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yoara
 */
public class SessionService {
    private static SessionService instance = null;
    private static Object lock = new Object();
    private static final int EXPIRY_TIME = 60 * 30;// 1800秒
    private static final int INTERVAL_EXPIRY_TIME = 30 * 1000;// 30秒启动延时线程
    private BridgeCache bridgeCache;

    /**
     * 活动的SessionId ，保存无需线程安全，
     **/
    private static ConcurrentHashMap<String,String> sessionIdMap = new ConcurrentHashMap();
    //private static HashSet<String> sessionIdSet = new HashSet<>();    //在多线程下会造成死循环；
    /**
     * 改造频繁调用redisCache.expire的方式二
     * session延时线程
     **/
    private Thread executer = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    //从缓存中读取需要延时的id
                    Set<String> cachedIdSet = sessionIdMap.keySet();
                    sessionIdMap = new ConcurrentHashMap<>();
                    String[] ids = cachedIdSet.toArray(new String[]{});
                    if (ids != null && ids.length > 0) {
                        //统一延时处理
                        for (String id : ids) {
                            bridgeCache.expire(CacheConstants.SESSION, id, EXPIRY_TIME);
                        }
                    }
                    Thread.sleep(INTERVAL_EXPIRY_TIME);
                } catch (Exception e) {}
            }
        }
    };

    public static SessionService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SessionService();
                    //instance.executer.start();
                }
            }
        }
        return instance;
    }

    private SessionService() {
        bridgeCache = (BridgeCache) ApplicationContextRegister.getApplicationContext().getBean("bridgeCache");
    }

    public Map<String, Object> getSession(String id) {
        Map<String, Object> session = null;
        if (session == null) {
            session = (Map<String, Object>) bridgeCache.get(CacheConstants.SESSION, id);
        }
        if (session == null) {
            session = new HashMap<>();
            saveSession(id, session);
        } else {
            //sessionIdMap.add(id,"");
        }
        return session;
    }

    public void saveSession(String id, Map<String, Object> session) {
        bridgeCache.put(CacheConstants.SESSION, id, session, EXPIRY_TIME);
    }

    public void removeSession(String id) {
        bridgeCache.remove(CacheConstants.SESSION, id);
        //sessionIdMap.remove(id);
    }

    /**
     * 改造频繁调用redisCache.expire的方式一
     * @param sessionId
     */
    public void expireSession(String sessionId) {
        bridgeCache.expire(CacheConstants.SESSION, sessionId, EXPIRY_TIME);
    }
}
