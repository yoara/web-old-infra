package org.yoara.framework.component.web.cache;

/**
 * 缓存接口，调用组件的子系统必须用本地缓存实现该接口的方法
 * Created by yoara on 2016/7/21.
 */
public interface BridgeCache {
    /**
     * 缓存键值对存储方法
     * @param type 缓存类型
     * @param key 缓存主键
     * @param value 缓存值
     * @param expire 超时时间
     */
    void put(String type,String key, Object value, int expire);

    /**
     * 删除缓存键值对
     * @param type 缓存类型
     * @param key 缓存主键
     */
    void remove(String type,String key);

    /**
     * 查询缓存键的值
     * @param type 缓存类型
     * @param key 缓存主键
     * @return
     */
    Object get(String type,String key);

    /**
     * 查询缓存键的有效时间
     * @param type 缓存类型
     * @param key 缓存主键
     * @return
     */
    long ttl(String type,String key);

    /**
     * 对缓存键进行延时操作
     * @param type 缓存类型
     * @param key 缓存主键
     * @return
     */
    void expire(String type, String key, int expiryTime);
}
