package org.yoara.framework.core.util;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ClassPathScanner extends
		ClassPathScanningCandidateComponentProvider {
	//懒汉单例
	private static ClassPathScanner scanner = new ClassPathScanner(true);
	public static ClassPathScanner getInstance(){
		return scanner;
	}

	private ClassPathScanner(boolean useDefaultFilters) {
		super(useDefaultFilters);
	}

	/**
	 * 类路径Bean定义扫描器扫描给定包及其子包，查找是否包含某注解
	 */
	public Set<String> doScan(Annotation annotation,String... basePackages) throws Exception {
		// 创建一个集合，存放扫描到Bean定义的封装类
		Set<String> requestAnnotation = new LinkedHashSet<>();
		// 遍历扫描所有给定的包
		for (String basePackage : basePackages) {
			// 调用父类ClassPathScanningCandidateComponentProvider的方法
			// 扫描给定类路径，获取符合条件的Bean定义
			Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
			// 遍历扫描到的Bean
			for (BeanDefinition candidate : candidates) {
				//扫描类，如果此类有注解，则记录,并且不再查找此类的方法
				AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) candidate;
				Map<String, Object> attributes = annDef.getMetadata().getAnnotationAttributes(annotation.getClass().getName());
				if(attributes!=null){
					requestAnnotation.add(candidate.getBeanClassName());
				}else{				
					getMethodAnnotationByClass(candidate,annotation,requestAnnotation);
				}
			}
		}
		return requestAnnotation;
	}
	//toRegisterGeneral1,registerGeneral,checkUserName
	protected void getMethodAnnotationByClass(BeanDefinition bean,Annotation annotation,Set<String> requestAnnotation)
			throws ClassNotFoundException{
		Class cs = Class.forName(bean.getBeanClassName());
		Method[] methods = cs.getMethods();
		for(int i=0;i<methods.length;i++){
			Method method = methods[i];
			Annotation methodAnnotation = method.getAnnotation(annotation.getClass());
			if(methodAnnotation!=null){//表明在这个方法上有这个注解
				requestAnnotation.add(bean.getBeanClassName());
			}
		}
	}

	public static void main(String[] args) throws Exception{
		System.out.println("*******************BEGIN***");
		String path="com.company.seed.controller";
		ClassPathScanner.getInstance().doScan(null,path);
	}
}
