package org.yoara.framework.core.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Map工具
 * Created by yoara on 2016/3/3.
 */
public class CommonMapUtil {
	
	public static double getDouble(Map<String, Object> map, String key) {
		return getDouble(map, key, 0);
	}
	
	public static double getDouble(Map<String, Object> map, String key, double defaultValue) {
		if (null == key || null == map) return defaultValue;
		try {
			return Double.parseDouble(map.get(key).toString());
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static int getInt(Map<String, Object> map, String key) {
		return getInt(map, key, 0);	
	}
	
	public static int getInt(Map<String, Object> map, String key, int defaultValue) {
		if (null == key || null == map) return defaultValue;
		try {
			String str = String.valueOf(map.get(key));
			if (StringUtils.isNotEmpty(str)) {
				str = str.replaceAll(",", "");
				return Integer.valueOf(str);
			}
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static boolean getBoolean(Map<String, Object> map, String key) {
		return getBoolean(map, key, false);
	}
	
	public static boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
		if (null == key || null == map) return defaultValue;
		try {
			return Boolean.valueOf(String.valueOf(map.get(key)));
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static String getString(Map<String, Object> map, String key) {
		return getString(map, key, null);
	}
	
	public static String getString(Map<String, Object> map, String key, String defaultValue) {
		if (null == key || null == map) return defaultValue;
		try {
			String val = String.valueOf(map.get(key));
			if (StringUtils.isNotEmpty(val)) {
				return val;
			}
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
		return getBigDecimal(map, key, null);
	}
	
	public static BigDecimal getBigDecimal(Map<String, Object> map, String key, BigDecimal defaultValue) {
		if (null == key || null == map) return defaultValue;
		try {
			BigDecimal val = (BigDecimal) map.get(key);
			if (null != val) {
				return val;
			}
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static Object getObject(Map<String, Object> map, String key) {
		return getObject(map, key, null);
	}
	
	public static Object getObject(Map<String, Object> map, Object key, Object defaultValue) {
		if (null == key || null == map) return defaultValue;
		Object val = map.get(key);
		if (null != val) {
			return val;
		}
		return defaultValue;
	}
}
