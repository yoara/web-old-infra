package org.yoara.framework.component.web.bean;

/**
 * @author yoara
 * @date 2015-3-10 下午3:38:16
 */
public interface JsonReturnCodeEnum {
	/**
	 * 返回码对应提示消息
	 * @date 2015-3-11 上午11:25:22
	 */
	String getMessage();
	
	/**
	 * 响应状态码
	 * @date 2015-3-11 上午11:25:52
	 */
	String getStatus();
}
