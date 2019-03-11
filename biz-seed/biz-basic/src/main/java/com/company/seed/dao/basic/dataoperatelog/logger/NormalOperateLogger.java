package com.company.seed.dao.basic.dataoperatelog.logger;

import com.company.seed.common.ContextHolder;
import com.company.seed.common.LoggerConstants;
import com.company.seed.dao.basic.dataoperatelog.LogObject;
import com.company.seed.dao.basic.dataoperatelog.LogObjectActor;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogField;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogMethod;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 标准日志记录输出，打印变化部分
 * Created by yoara on 2017/1/6.
 */

public class NormalOperateLogger extends BaseOperateLogger{
	private Logger logger = LoggerFactory.getLogger(LoggerConstants.SQLLOGGER);
	@Override
	protected void doAct(LogObject operateLog){
		DataOperateLogMethod operateLogMethod = operateLog.getOperateLogMethod();
		Field[]fields = operateLog.getEntityFields();
		Object entity = operateLog.getEntity();
		Object historyObj = operateLog.getHistoryObj();

		LogObjectActor actor = () ->{
			StringBuffer content = new StringBuffer("[normalOperate]");
			content.append(ContextHolder.getDataSource().name()).append(":\t");
			content.append("tableName:").append(operateLog.getEntityName()).append("\t");
			content.append("desc:").append(operateLog.getEntityDesc()).append("\t");
			content.append("opeType:").append(operateLog.getOperateType()).append("\t");
			content.append("logType:").append(operateLog.getLogType()).append("\t");
			content.append("doneOperate:").append(operateLog.isDoneOperate()).append("\t");
			if(StringUtils.isNotEmpty(operateLog.getAdditionalComments())){
				content.append("remark:").append(operateLog.getAdditionalComments()).append("\t");
			}
			content.append("\r\n");
			for(Field field:fields){
				DataOperateLogField operateLogField = field.getAnnotation(DataOperateLogField.class);
				if(operateLogField==null){
					continue;
				}
				String historyValue = historyObj==null
						?null:BeanUtils.getProperty(historyObj,field.getName());
				String nowValue = BeanUtils.getProperty(entity,field.getName());

				if(StringUtils.isNotEmpty(nowValue) || !operateLogMethod.ignoreNull()){
					content.append("├─").append(field.getName())
							.append("[").append(operateLogField.field()).append(":")
							.append(operateLogField.desc()).append("]")
							.append("( ").append(historyValue).append(" => ").append(nowValue)
							.append(" )").append("\r\n");
				}
			}
			logger.info(content.toString());
		};
		operateLog.setActor(actor);
	}
}
