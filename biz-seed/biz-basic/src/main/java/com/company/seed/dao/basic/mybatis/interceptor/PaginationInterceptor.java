package com.company.seed.dao.basic.mybatis.interceptor;

import com.company.seed.dao.basic.mybatis.dialect.Dialect;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;


/**
 * 分页拦截器, 支持多种数据库
 * Created by yoara on 2015/12/21.
 */
@Intercepts({@Signature(
		type= Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor extends AbstractInterceptor {
	private static int MAPPED_STATEMENT_INDEX = 0;
	private static int PARAMETER_INDEX = 1;
	private static int ROWBOUNDS_INDEX = 2;

	private Dialect dialect;

	public Object intercept(Invocation invocation) throws Throwable {
		processIntercept(invocation.getArgs());
		return invocation.proceed();
	}

	private void processIntercept(final Object[] queryArgs) {
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[PARAMETER_INDEX];
		final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		//分页
		if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
			BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql().trim();
			if (dialect.supportsLimitOffset()) {
				sql = dialect.getLimitString(sql, offset, limit);
				offset = RowBounds.NO_ROW_OFFSET;
			} else {
				sql = dialect.getLimitString(sql, 0, limit);
			}
			limit = RowBounds.NO_ROW_LIMIT;
			queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
			//BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			try {
				FieldUtils.writeDeclaredField(boundSql, "sql", sql, true);
			} catch (IllegalAccessException e) {}
			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(boundSql), false);
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
		} else if(parameter instanceof CountParameter) {
			//获取总数
			parameter = ((CountParameter) parameter).getParameter();
			BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql().trim().toLowerCase();
			sql = sql.replaceAll("	", " ");
			if(sql.indexOf("group")!=-1 || sql.indexOf("distinct")!=1){// || sql.indexOf("order")!=1
				sql = "select count(1) from (" + sql + ") tmp"; //性能差
			}else{
				sql=sql.replaceAll("^select *(?:(?!select|from)[\\s\\S])*(\\( *select *(?:(?!from)[\\s\\S])* *from *[^\\)]*\\)(?:(?!select|from)[\\s\\S])*)*(?:(?!select|from)[\\s\\S])*from", "select count(*) from");
			}
			try {
				FieldUtils.writeDeclaredField(boundSql, "sql", sql, true);
			} catch (IllegalAccessException e) {}
			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(boundSql), true);
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
			queryArgs[PARAMETER_INDEX] = parameter;
		}
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		String dialectClass = properties.getProperty("dialectClass");
		try {
			dialect = (Dialect) Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(
					"cannot create dialect instance by dialectClass:"
							+ dialectClass, e);
		}
	}

	public static class CountParameter {
		private Object parameter;
		public CountParameter(Object parameter) {
			this.parameter = parameter;
		}
		public Object getParameter() {
			return parameter;
		}
	}

}
