package org.yoara.framework.core.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class VelocityUtil {


	//-------------------------将邮件的volocity抽取出来------------------------------------
	//-------------------------------start---------------------------------------------

	private static VelocityEngine engine;

	static{
		engine = new VelocityEngine();

		//初始化参数
		Properties properties=new Properties();
		//设置velocity资源加载方式为class
		properties.setProperty("resource.loader", "class");
		//设置velocity资源加载方式为file时的处理类
		properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		try {
			engine.init(properties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 渲染指定的模板文件
	 * @param fileUrl 文件的url
	 * @param model	  数据模型
	 * @return	渲染后的数据
	 */
	public static String templateMerge(String fileUrl, Map<String, Object> model){

		Template template;
		StringWriter writer = new StringWriter();;
		try {
			template = engine.getTemplate(fileUrl, "UTF-8");

			template.merge(convertVelocityContext(model), writer);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}



		return writer.toString();
	}

	//-------------------------------end---------------------------------------------


	public static String evaluate(Map<String, Object> model, String strTemplate){

		//输出流
		StringWriter writer = new StringWriter();

		//渲染模板
		try {
			Velocity.evaluate(convertVelocityContext(model), writer, "", strTemplate);
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}

		return writer.toString();
	}


	/**
	 * 把Map转换成Context
	 */
	@SuppressWarnings("unused")
	private static VelocityContext convertVelocityContext(Map<String, Object> map) {
		VelocityContext context = new VelocityContext();
		if (map == null) {
			return context;
		}
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
		return context;
	}

	public static void main(String[] args) throws Exception {

		// 取得velocity的模版内容, 模板内容来自字符传

		String content = "";
		content += "Welcome  ${name}to Javayou.com! #if($name)${name}#else${date}#end";
		//content += " today is  $date.";

		// 取得velocity的上下文context
		VelocityContext context = new VelocityContext();

		// 把数据填入上下文
		//context.put("name", "javaboy2012");

		context.put("date", (new Date()).toString());

		// 输出流
		StringWriter writer = new StringWriter();

		// 转换输出

		Velocity.evaluate(context, writer, "", content); // 关键方法

		System.out.println(writer.toString());

	}
}
