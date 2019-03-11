package org.yoara.framework.component.web.bean;

/**
 * Created by yoara on 2016/3/2.
 */
public class JsonReturnObject {
	
	private String message;
	
	private Object result;
	
	private String status;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
