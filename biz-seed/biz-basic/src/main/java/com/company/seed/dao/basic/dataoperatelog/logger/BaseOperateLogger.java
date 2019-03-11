package com.company.seed.dao.basic.dataoperatelog.logger;

import com.company.seed.dao.basic.dataoperatelog.LogObject;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogEntity;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogMethod;
import com.company.seed.model.Entity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yoara on 2017/1/6.
 */

public abstract class BaseOperateLogger{
	/**
	 * 子类实现具体的判断逻辑
	 * <p>参数中行为字段</p>
	 * <ul>
	 *     <li>fields 指定类型Model的变量集合</li>
	 *     <li>entity 可能是Model类型，也可能是Map</li>
	 *     <li>historyObj selectById的返回结果，通常情况下是具体的Model类型</li>
	 *     <li>operateLogMethod 方法操作注解</li>
	 * </ul>
     */
	protected abstract void doAct(LogObject operateLog);
	/**
	 * 记录操作日志
	 */
	public LogObject doLog(DataOperateLogMethod operateLogMethod, Object entity, Object historyObj) throws Exception {
		Class operateClazz = Entity.class.equals(operateLogMethod.operateEntity())?
				entity.getClass():operateLogMethod.operateEntity();
		DataOperateLogEntity operateLogEntity = (DataOperateLogEntity)operateClazz.getAnnotation(DataOperateLogEntity.class);

		String table = StringUtils.isEmpty(operateLogEntity.table())
				?operateClazz.getSimpleName():operateLogEntity.table();

		//日志对象
		LogObject operateLog = new LogObject();
		operateLog.setOperateType(operateLogMethod.value());
		operateLog.setLogType(operateLogMethod.loggerType());
		String id = BeanUtils.getProperty(entity,operateLogEntity.id());
		operateLog.setId(id);
		operateLog.setEntityName(table);
		operateLog.setEntityDesc(operateLogEntity.desc());
		operateLog.setEntityFields(operateClazz.getDeclaredFields());
		operateLog.setEntity(entity);
		operateLog.setHistoryObj(historyObj);
		operateLog.setOperateLogMethod(operateLogMethod);

		this.doAct(operateLog);

		return operateLog;
	}
}
