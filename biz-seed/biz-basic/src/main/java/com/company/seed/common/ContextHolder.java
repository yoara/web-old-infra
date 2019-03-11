package com.company.seed.common;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>上下文变量</p>
 * 所有需要在本地线程存储的数据都存放在这，key-value形式
 */
public class ContextHolder {
	private static final ThreadLocal<Map<String, Object>> contextHolder
			= new ThreadLocal<>();
	private static final String DATA_SOURCE = "DATA_SOURCE";

	public static void setDataSource(DataSourceEnum dataSource) {
		Map<String, Object> holder = getContextHolder();
		holder.put(DATA_SOURCE, dataSource);
	}

	public static DataSourceEnum getDataSource() {
		Map<String, Object> holder = getContextHolder();
		return holder.get(DATA_SOURCE) == null
				?DataSourceEnum.USER:(DataSourceEnum)holder.get(DATA_SOURCE);
	}

	private static Map<String, Object> getContextHolder(){
		Map<String, Object> holder = contextHolder.get();
		if(holder == null) {
			holder = new HashMap<>();
			contextHolder.set(holder);
		}
		return holder;
	}
}
