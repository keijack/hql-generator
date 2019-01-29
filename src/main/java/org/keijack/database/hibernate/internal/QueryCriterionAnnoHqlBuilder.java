package org.keijack.database.hibernate.internal;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.keijack.database.hibernate.stereotype.EmbedType;

/**
 * 
 * @author Keijack
 * 
 */
public abstract class QueryCriterionAnnoHqlBuilder {

	/**
	 * 类别名
	 */
	private String alias;

	protected String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 获得由 SQLFunction 组成的属性查询条件左边
	 * 
	 * @param conditionAnno
	 * @return
	 */
	protected StringBuilder getHqlFieldWithSqlFunction(QueryCriterionInfo conditionAnno) {
		StringBuilder filedWithSqlFunction = new StringBuilder();
		filedWithSqlFunction
				.append(conditionAnno.getHqlFunction().getName())
				.append("(").append(this.getAlias()).append(".")
				.append(conditionAnno.getField()).append(")");
		return filedWithSqlFunction;
	}

	/**
	 * 
	 * @param annoInfo 条件标注
	 * @param obj      值
	 * @param where    Hql收集参数
	 * @param params   参数收集参数
	 */
	public void generateHql(QueryCriterionInfo annoInfo, Object obj, StringBuilder where,
			Map<String, Object> params) {
		StringBuilder wh = new StringBuilder(" and ");
		Map<String, Object> pas = new LinkedHashMap<>();

		if (obj == null) {
			return;
		} else if (EmbedType.NONE.equals(annoInfo.getEmbedType()) || !Collection.class.isInstance(obj)) {
			this.generateHqlFragment(annoInfo, obj, wh, pas);
		} else if (((Collection<?>) obj).isEmpty()) {
			return;
		} else if (((Collection<?>) obj).size() == 1) {
			this.generateHqlFragment(annoInfo, ((Collection<?>) obj).toArray(new Object[1])[0], wh, pas);
		} else {
			StringBuilder w = new StringBuilder("(");
			Map<String, Object> p = new LinkedHashMap<>();
			int i = 0;
			for (Object o : (Collection<?>) obj) {
				annoInfo.setSeq(i++);
				if (w.toString().length() != 1) {
					w.append(" " + annoInfo.getEmbedType().toString().toLowerCase() + " ");
				}
				this.generateHqlFragment(annoInfo, o, w, p);
			}
			w.append(")");

			wh.append(w.toString());
			pas.putAll(p);
		}

		where.append(wh.toString());
		params.putAll(pas);
	}

	/**
	 * 
	 * @param conditionAnno 条件标注
	 * @param param         值
	 * @param where         Hql收集参数
	 * @param params        参数收集参数
	 */
	public abstract void generateHqlFragment(QueryCriterionInfo conditionAnno, Object param, StringBuilder where,
			Map<String, Object> params);
}