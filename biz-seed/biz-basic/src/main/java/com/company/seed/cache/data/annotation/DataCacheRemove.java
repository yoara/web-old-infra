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
public @interface DataCacheRemove {
    /**
     * 缓存名
     * @return
     */
    CacheTypeEnum value();

    /**
     * <P>缓存键</P>
     * 使用下标表示参数索引位
     * @see {{@link DataCacheAble}}
     * @return
     */
    String[] key() default {};

    /**
     * 清空全部
     * @return
     */
    boolean allEntries() default false;

}
