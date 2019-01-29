package org.keijack.database.hibernate;

import org.keijack.database.hibernate.internal.HqlFromGenerator;
import org.keijack.database.hibernate.internal.HqlOrderByGenerator;
import org.keijack.database.hibernate.internal.HqlWhereGenerator;
import org.keijack.database.hibernate.internal.util.ReflectionUtil;
import org.keijack.database.hibernate.stereotype.QueryParamsFor;

/**
 * 
 * @author Keijack
 * 
 */
public final class HqlGenerator {

	/**
	 * 不能被实例化
	 */
	private HqlGenerator() {
		super();
	}

	/**
	 * 
	 * @param queryParamsObj 获得一个Hql, 这个类必须被标注为 QueryParamsFor
	 * @return HqlAndParams
	 */
	public static HqlAndParams generateHql(Object queryParamsObj) {

		HqlAndParams hqlAndParams = new HqlAndParams();

		QueryParamsFor queryParamsForAnno = ReflectionUtil.getClassAnnotationRecursively(queryParamsObj,
				QueryParamsFor.class);

		String from = new HqlFromGenerator(queryParamsForAnno).getFrom();
		hqlAndParams.setFrom(from);

		HqlWhereGenerator whereGenerator = new HqlWhereGenerator(queryParamsForAnno, queryParamsObj);
		hqlAndParams.setWhere(whereGenerator.getWhere());
		hqlAndParams.setParams(whereGenerator.getParams());

		String orderBy = new HqlOrderByGenerator(queryParamsForAnno,
				queryParamsObj).getOrderBy();
		hqlAndParams.setOrderBy(orderBy);

		return hqlAndParams;
	}

}
