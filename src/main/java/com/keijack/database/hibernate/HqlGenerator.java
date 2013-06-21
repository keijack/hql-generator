package com.keijack.database.hibernate;

import com.keijack.database.hibernate.internal.HqlFromGenerator;
import com.keijack.database.hibernate.internal.HqlOrderByGenerator;
import com.keijack.database.hibernate.internal.HqlWhereGenerator;
import com.keijack.database.hibernate.stereotype.QueryParamsFor;
import com.keijack.database.hibernate.util.ReflectionUtil;

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
     * @param queryParamsObj
     *            获得一个Hql, 这个类必须被标注为 QueryParamsFor
     * @return
     * @throws HqlGeneratException
     */
    public static HqlAndParams generateHql(Object queryParamsObj)
	    throws HqlGeneratException {

	HqlAndParams hqlAndParams = new HqlAndParams();

	QueryParamsFor queryParamsForAnno = ReflectionUtil
		.getClassAnnotationRecursively(queryParamsObj,
			QueryParamsFor.class);

	String from = new HqlFromGenerator(queryParamsForAnno).getFrom();
	hqlAndParams.setFrom(from);

	HqlWhereGenerator whereGenerator = new HqlWhereGenerator(
		queryParamsForAnno, queryParamsObj);
	hqlAndParams.setWhere(whereGenerator.getWhere());
	hqlAndParams.setParams(whereGenerator.getParams());

	String orderBy = new HqlOrderByGenerator(queryParamsForAnno,
		queryParamsObj).getOrderBy();
	hqlAndParams.setOrderBy(orderBy);

	return hqlAndParams;
    }

}
