package org.keijack.database.hibernate.internal;

import org.keijack.database.hibernate.stereotype.QueryParamsFor;

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

	/**
	 * from
	 */
	private String from;

	public HqlFromGenerator(QueryParamsFor queryParamsForAnno) {
		super();
		this.queryParamsForAnno = queryParamsForAnno;
	}

	public String getFrom() {
		if (from == null) {
			generate();
		}
		return from;
	}

	public void generate() {
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
				if (i != (selectFields.length - 1)) {
					selectString.append(",");
				}
				selectString.append(" ");
			}

		}
		this.from = selectString.toString() + "from " + modelClass.getName()
				+ " " + alias;
	}
}
