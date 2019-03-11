package com.company.seed.cache.data.annotation;


import com.company.seed.common.CacheTypeEnum;

import java.lang.annotation.*;

/**
 * Created by yoara on 2017/1/3.
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataCacheAble {
    /**
     * 缓存名
     * @return
     */
    CacheTypeEnum value();

    /**
     * <P>缓存键，若为空则以所有参数作为key值索引缓存</P>
     * 使用下标表示参数索引位：<br/>
     * <ul>
     *  <li>list:[0]</li>
     *  <li>map:['xxx']</li>
     *  <li>object:xxx.xxx</li>
     *  <li>String:#this</li>
     * </ul>
     * <h3>"#1.list[0]"</h3>
     * <h3>"#1.map['xxx']"</h3>
     * <h3>"#1.xxx.xxx"</h3>
     * <h3>"#1.#this"</h3>
     * @return
     */
    String[] key() default {};

    /**
     * 缓存条件，遵循spring 表达式写法
     * 使用下标表示参数索引位：<br/>
     * @see {@link #key()}
     * @return
     */
    String[] condition() default {};

    /**
     * 缓存时间
     * @return
     */
    int time() default 0;

    /**
     * 缓存时间单位
     * @return
     */
    DataCacheTimeUnit timeUnit() default DataCacheTimeUnit.minute;

}
