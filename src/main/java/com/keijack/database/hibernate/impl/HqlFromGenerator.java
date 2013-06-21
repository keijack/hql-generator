package com.keijack.database.hibernate.impl;

import com.keijack.database.hibernate.hqlconditions.QueryParamsFor;

/**
 * Generate hql's from part.
 * 
 * @author keijack.wu
 * 
 */
public class HqlFromGenerator {

    /**
     * Generate hql's from with listParamsAnno;
     */
    private final QueryParamsFor queryParamsForAnno;

    public HqlFromGenerator(QueryParamsFor queryParamsForAnno) {
	super();
	this.queryParamsForAnno = queryParamsForAnno;
    }

    /**
     * @param listParamsObj
     *            ListCall
     * @param hqlResult
     *            收集参数
     */
    public String getFrom() {
	String alias = queryParamsForAnno.alias();
	Class<?> modelClass = queryParamsForAnno.value();
	StringBuilder selectString = new StringBuilder();
	String[] selectFields = queryParamsForAnno.fields();
	if (selectFields != null && selectFields.length > 0) {
	    selectString.append("select ");
	    if (queryParamsForAnno.distinct()) {
		selectString.append("distinct ");
	    }
	    for (int i = 0; i < selectFields.length; i++) {

		selectString.append(alias).append(".").append(selectFields[i]);
		if (i != 1) {
		    selectString.append(",");
		}
		selectString.append(" ");
	    }

	}
	return selectString.toString() + "from " + modelClass.getName() + " "
		+ alias;
    }
}
