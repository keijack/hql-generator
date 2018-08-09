package org.keijack.database.hibernate.internal;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
	 * @param conditionAnno 条件标注
	 * @param obj           值
	 * @param where         Hql收集参数
	 * @param params        参数收集参数
	 */
	public void generateHql(QueryCriterionInfo conditionAnno, Object obj, StringBuilder where,
			List<Object> params) {
		StringBuilder wh = new StringBuilder(" and ");
		List<Object> pas = new LinkedList<>();

		if (obj == null) {
			return;
		} else if (EmbedType.NONE.equals(conditionAnno.getEmbedType()) || !Collection.class.isInstance(obj)) {
			this.generateHqlFragment(conditionAnno, obj, wh, pas);
		} else if (((Collection<?>) obj).isEmpty()) {
			return;
		} else if (((Collection<?>) obj).size() == 1) {
			this.generateHqlFragment(conditionAnno, ((Collection<?>) obj).toArray(new Object[1])[0], wh, pas);
		} else {
			StringBuilder w = new StringBuilder("(");
			List<Object> p = new LinkedList<>();
			for (Object o : (Collection<?>) obj) {
				if (w.toString().length() != 1) {
					w.append(" " + conditionAnno.getEmbedType().toString().toLowerCase() + " ");
				}
				this.generateHqlFragment(conditionAnno, o, w, p);
			}
			w.append(")");

			wh.append(w.toString());
			pas.addAll(p);
		}

		where.append(wh.toString());
		params.addAll(pas);
	}

	/**
	 * 
	 * @param conditionAnno 条件标注
	 * @param param         值
	 * @param where         Hql收集参数
	 * @param params        参数收集参数
	 */
	public abstract void generateHqlFragment(QueryCriterionInfo conditionAnno, Object param, StringBuilder where,
			List<Object> params);
}