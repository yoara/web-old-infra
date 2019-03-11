/**
 * 
 */
package org.yoara.framework.component.web.bean.resultfilter;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * FastJson 属性过滤器
 * 可以设置多个类的对应属性(包含，不包含)过滤，（同一个class包含和不包含只能使用一个）
 * @author HeYuanxun
 * @date 2014-12-26 下午6:12:58
 */
public class MultipleClassPropertyPreFilter implements PropertyPreFilter {
	
	// 包含属性的集合
	private final Map<Class<?>, Set<String>> includes = new HashMap<Class<?>, Set<String>>();
	
	// 不包含属性的集合
	private final Map<Class<?>, Set<String>> excludes = new HashMap<Class<?>, Set<String>>();
	
	private boolean empty = true;
	
	/**
	 * 设置包含的属性。相同的Class实例可以重复设置，在原有的基础上累加
	 * @param clazz
	 * @param properties
	 * @author HeYuanxun
	 * @date 2014-12-29 上午9:07:49
	 */
	public void putInclude(Class<?> clazz, String... properties) {
		put(clazz, true, properties);
	}
	
	/**
	 * 设置不包含的属性。相同的Class实例可以重复设置，在原有的基础上累加
	 * @param clazz
	 * @param properties
	 * @author HeYuanxun
	 * @date 2014-12-29 上午9:08:39
	 */
	public void putExclud(Class<?> clazz, String... properties) {
		put(clazz, false, properties);
	}
	
	private void put(Class<?> clazz, boolean include, String... properties) {
		if(clazz == null) {
			throw new IllegalArgumentException("clazz must not be null");
		}
		
		if (properties == null || properties.length <= 0) {
			// 过滤的属性列表为空，无需过滤
			return;
		}
		
		if (include) {
			if (excludes.containsKey(clazz)) {
				throw new IllegalStateException("The class '" + clazz + "' was already set exclude.");
			}
			Set<String> includeSet = includes.get(clazz);
			if (includeSet == null) { // 类还没有创建包含属性，创建一个
				includeSet = new HashSet<String>();
				includes.put(clazz, includeSet);
			}
			for (String property : properties) {
				includeSet.add(property); // 添加包含属性
			}
		} else {
			if (includes.containsKey(clazz)) {
				throw new IllegalStateException("The class '" + clazz + "' was already set include.");
			}
			Set<String> excludeSet = excludes.get(clazz);
			if(excludeSet == null) {
				excludeSet = new HashSet<String>();
				excludes.put(clazz, excludeSet);
			}
			for (String property : properties) {
				excludeSet.add(property);
			}
		}
		
		empty = false;
	}
	
	/* (non-Javadoc)
	 * @see com.alibaba.fastjson.serializer.PropertyPreFilter#apply(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean apply(JSONSerializer serializer, Object object, String name) {
		if (object == null) {
            return true;
        }
		
		if(empty) {
			return true;
		}
		
		Class<?> clazz = object.getClass();
		if (excludes.containsKey(clazz)) {
			if (this.excludes.get(clazz).contains(name)) {
	            return false;
	        } else {
	        	return true;
	        }
		}
		
		if (includes.containsKey(clazz)) {
			if (this.includes.get(clazz).contains(name)) {
				return true;
			} else {
				return false;
			}
		}
		
		return true;
	}

}
