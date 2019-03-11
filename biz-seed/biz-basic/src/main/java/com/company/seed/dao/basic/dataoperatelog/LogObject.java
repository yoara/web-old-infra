package com.company.seed.dao.basic.dataoperatelog;

import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogMethod;
import com.company.seed.dao.basic.dataoperatelog.enums.DataLoggerTypeEnum;
import com.company.seed.dao.basic.dataoperatelog.enums.DataOperateTypeEnum;

import java.lang.reflect.Field;

/**
 * 日志信息记录类
 * Created by yoara on 2017/1/6.
 */
public class LogObject {
    private boolean doneOperate = true;
    private DataLoggerTypeEnum logType;
    private DataOperateTypeEnum operateType;
    private String entityName;
    private String entityDesc;
    private String id;
    private LogObjectActor actor;
    private String additionalComments;
    /**
     * 用于logActor行为提供参数
     */
    private Object entity;
    private Object historyObj;
    private DataOperateLogMethod operateLogMethod;
    private Field[] entityFields;

    public void setActor(LogObjectActor actor) {
        this.actor = actor;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Object getHistoryObj() {
        return historyObj;
    }

    public void setHistoryObj(Object historyObj) {
        this.historyObj = historyObj;
    }

    public DataOperateLogMethod getOperateLogMethod() {
        return operateLogMethod;
    }

    public void setOperateLogMethod(DataOperateLogMethod operateLogMethod) {
        this.operateLogMethod = operateLogMethod;
    }

    public Field[] getEntityFields() {
        return entityFields;
    }

    public void setEntityFields(Field[] entityFields) {
        this.entityFields = entityFields;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataOperateTypeEnum getOperateType() {
        return operateType;
    }

    public void setOperateType(DataOperateTypeEnum operateType) {
        this.operateType = operateType;
    }

    public DataLoggerTypeEnum getLogType() {
        return logType;
    }

    public void setLogType(DataLoggerTypeEnum logType) {
        this.logType = logType;
    }

    public boolean isDoneOperate() {
        return doneOperate;
    }

    public void setDoneOperate(boolean doneOperate) {
        this.doneOperate = doneOperate;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public String getEntityDesc() {
        return entityDesc;
    }

    public void setEntityDesc(String entityDesc) {
        this.entityDesc = entityDesc;
    }

    public void doAct() throws Exception {
        actor.doAct();
    }
}
