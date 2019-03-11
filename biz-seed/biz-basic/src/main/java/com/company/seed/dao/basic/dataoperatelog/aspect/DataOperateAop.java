package com.company.seed.dao.basic.dataoperatelog.aspect;

import com.alibaba.fastjson.util.TypeUtils;
import com.company.seed.dao.basic.dataoperatelog.LogObject;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogEntity;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogMethod;
import com.company.seed.dao.basic.dataoperatelog.logger.DataLoggerFactory;
import com.company.seed.model.Entity;
import com.company.seed.threads.ThreadFixedPool;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 操作记录AOP
 * Created by yoara on 2017/01/06.
 */
@Component
@Aspect
public class DataOperateAop {
	private static Logger aopLogger = LoggerFactory.getLogger(DataOperateAop.class);

	/**
	 * 待记录操作
	 */
	@Around(value = "@annotation(operateLogMethod)")
	public Object doLogProcess(ProceedingJoinPoint pjp,DataOperateLogMethod operateLogMethod) throws Throwable {
		if(operateLogMethod.async()){
			return doAsyncLogProcess(pjp,operateLogMethod);
		}else{
			return doSyncLogProcess(pjp,operateLogMethod);
		}
	}

	private Object doSyncLogProcess(ProceedingJoinPoint pjp,DataOperateLogMethod operateLogMethod) throws Throwable {
		LogObject logObject = recordLog(pjp,operateLogMethod);
		try{
			return pjp.proceed(pjp.getArgs());
		}catch (Exception e){
			if(logObject!=null){
				logObject.setDoneOperate(false);
				logObject.setAdditionalComments(e.getMessage());
			}
			throw e;
		}finally{
			if(logObject!=null){
				try{
					logObject.doAct();
				}catch (Exception e){
					aopLogger.error("some thing wrong when act:"+e.getMessage());
				}
			}
		}
	}

	private Object doAsyncLogProcess(ProceedingJoinPoint pjp,DataOperateLogMethod operateLogMethod) throws Throwable {
		Future<LogObject> future = ThreadFixedPool.call(
				ThreadFixedPool.Key.DATA_OPERATE_AOP_START,() -> recordLog(pjp,operateLogMethod));
		boolean doneOperate = true;
		String additionalComments = null;
		try{
			return pjp.proceed(pjp.getArgs());
		}catch (Exception e){
			doneOperate = false;
			additionalComments = e.getMessage();
			throw e;
		}finally{
			try{
				LogObject logObject = future.get(10, TimeUnit.SECONDS);
				if(logObject!=null) {
					logObject.setDoneOperate(doneOperate);
					logObject.setAdditionalComments(additionalComments);
					ThreadFixedPool.submit(
							ThreadFixedPool.Key.DATA_OPERATE_AOP_END, () -> {
								try {
									logObject.doAct();
								} catch (Exception e) {
									aopLogger.error("some thing wrong when act:"+e.getMessage());
								}
							}
					);
				}
			}catch (Exception e){
				aopLogger.error("some thing wrong when act:"+e.getMessage());
			}
		}
	}

	/**
	 * 记录日志
	 */
	private LogObject recordLog(ProceedingJoinPoint jp,DataOperateLogMethod operateLogMethod){
		try{
			//查询出更改前对象值
			Object entity = getOperateData(jp,operateLogMethod);
			if(entity == null){
				return null;
			}
			Object historyObj = queryHistoryObject(entity,operateLogMethod, jp.getTarget());

			switch (operateLogMethod.loggerType()){
				case NORMAL:

				default:
					return DataLoggerFactory.getNormalLogger().doLog(operateLogMethod, entity, historyObj);
			}
		}catch (Exception e){
			aopLogger.error("some thing wrong in recordLog:"+e.getMessage());
			return null;
		}
	}

	/**
	 * 从参数列表中获取待记录的对象
	 */
	private Object getOperateData(JoinPoint pjp, DataOperateLogMethod operateLogMethod){
		Object entity;
		 /*获取参数*/
		try{
			entity = pjp.getArgs()[operateLogMethod.index()];
		}catch (Exception e){
			aopLogger.error("wrong args set in index["+operateLogMethod.index()+"]:"+e.getMessage());
			return null;
		}
		//校记录的实体是否属于DataOperateLogEntity标注
		Class operateClazz = Entity.class.equals(operateLogMethod.operateEntity())?
				entity.getClass():operateLogMethod.operateEntity();
		if(!operateClazz.isAnnotationPresent(DataOperateLogEntity.class)){
			aopLogger.warn("this entity have no DataOperateLogEntity["+operateClazz+"]:");
			return null;
		}
		return entity;
	}

	/**
	 * 获取历史对象
	 */
	private Object queryHistoryObject(Object entity
			,DataOperateLogMethod operateLogMethod, Object targetMethodClazz){
		try{
			Class operateClazz = Entity.class.equals(operateLogMethod.operateEntity())?
					entity.getClass():operateLogMethod.operateEntity();
			DataOperateLogEntity operateLogEntity =
					(DataOperateLogEntity)operateClazz.getAnnotation(DataOperateLogEntity.class);

			String methodName = operateLogEntity.method();
			Method method = ReflectionUtils.findMethod(
					targetMethodClazz.getClass(), methodName ,operateLogEntity.idType());

			String id = BeanUtils.getProperty(entity,operateLogEntity.id());
			if(StringUtils.isNotBlank(id)){
				return method.invoke(targetMethodClazz , TypeUtils.castToJavaBean(id,operateLogEntity.idType()));
			}else{
				aopLogger.error("no id found in entity");
			}
		}catch (Exception e){
			aopLogger.error("something wrong in queryHistoryObject:"+e.getMessage());
		}
		return null;
	}
}
