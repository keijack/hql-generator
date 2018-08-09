package org.keijack.database.hibernate.internal;

import java.util.EnumMap;

import org.keijack.database.hibernate.stereotype.ComparisonType;
import org.keijack.database.hibernate.stereotype.EmbedType;
import org.keijack.database.hibernate.stereotype.HqlFunctions;
import org.keijack.database.hibernate.stereotype.QueryCondition;
import org.keijack.database.hibernate.stereotype.QueryCriterion;
import org.keijack.database.hibernate.stereotype.RestrictionType;

class QueryCriterionInfo {

	private static final EnumMap<ComparisonType, RestrictionType> C_R = new EnumMap<>(ComparisonType.class);
	static {
		C_R.put(ComparisonType.EQUAL, RestrictionType.EQUAL);
		C_R.put(ComparisonType.NOTEQUAL, RestrictionType.NOT_EQUAL);
		C_R.put(ComparisonType.MORE, RestrictionType.MORE);
		C_R.put(ComparisonType.MOREEQUAL, RestrictionType.MORE_EQUAL);
		C_R.put(ComparisonType.LESS, RestrictionType.LESS);
		C_R.put(ComparisonType.LESSEQUAL, RestrictionType.LESS_EQUAL);
		C_R.put(ComparisonType.IN, RestrictionType.IN);
		C_R.put(ComparisonType.NOTIN, RestrictionType.NOT_IN);
		C_R.put(ComparisonType.LIKE, RestrictionType.LIKE);
		C_R.put(ComparisonType.NOTLIKE, RestrictionType.NOT_LIKE);
		C_R.put(ComparisonType.CONTAINS, RestrictionType.CONTAINS);
		C_R.put(ComparisonType.NOTCONTAINS, RestrictionType.NOT_CONTAINS);
		C_R.put(ComparisonType.ISNULL, RestrictionType.IS_NULL);
	}

	private final RestrictionType restriction;

	private final String field;

	private final EmbedType embedType;

	private final String preString;

	private final String postString;

	private final HqlFunctions hqlFunction;

	private final boolean emptyAsNull;

	public QueryCriterionInfo(QueryCriterion criterion) {
		this.restriction = criterion.restriction();
		this.field = criterion.field();
		this.embedType = criterion.embedType();
		this.preString = criterion.preString();
		this.postString = criterion.postString();
		this.hqlFunction = criterion.hqlFunction();
		this.emptyAsNull = criterion.emptyAsNull();
	}

	public QueryCriterionInfo(QueryCondition criterion) {
		this.restriction = C_R.get(criterion.comparison());
		this.field = criterion.field();
		this.embedType = criterion.embedType();
		this.preString = criterion.preString();
		this.postString = criterion.postString();
		this.hqlFunction = criterion.hqlFunction();
		this.emptyAsNull = criterion.emptyAsNull();
	}

	public RestrictionType getRestriction() {
		return restriction;
	}

	public String getField() {
		return field;
	}

	public EmbedType getEmbedType() {
		return embedType;
	}

	public String getPreString() {
		return preString;
	}

	public String getPostString() {
		return postString;
	}

	public HqlFunctions getHqlFunction() {
		return hqlFunction;
	}

	public boolean isEmptyAsNull() {
		return emptyAsNull;
	}

}
