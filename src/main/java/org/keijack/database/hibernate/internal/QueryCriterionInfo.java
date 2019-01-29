package org.keijack.database.hibernate.internal;

import java.lang.reflect.Field;

import org.keijack.database.hibernate.stereotype.EmbedType;
import org.keijack.database.hibernate.stereotype.HqlFunctions;
import org.keijack.database.hibernate.stereotype.QueryCriterion;
import org.keijack.database.hibernate.stereotype.RestrictionType;

class QueryCriterionInfo {

	private final String beanFieldName;

	/**
	 * If the annotation decorate a list, this may show which element in the list
	 */
	private Integer seq;

	private final RestrictionType restriction;

	private final String field;

	private final EmbedType embedType;

	private final String preString;

	private final String postString;

	private final HqlFunctions hqlFunction;

	private final boolean emptyAsNull;

	QueryCriterionInfo(Field field) {
		this.beanFieldName = field.getName();

		QueryCriterion criterion = field.getAnnotation(QueryCriterion.class);
		this.restriction = criterion.restriction();
		this.field = criterion.field();
		this.embedType = criterion.embedType();
		this.preString = criterion.preString();
		this.postString = criterion.postString();
		this.hqlFunction = criterion.hqlFunction();
		this.emptyAsNull = criterion.emptyAsNull();
	}

	public String getParamKey() {
		if (this.getSeq() == null) {
			return this.getBeanFieldName();
		} else {
			return this.getBeanFieldName() + this.getSeq();
		}
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getBeanFieldName() {
		return beanFieldName;
	}

	RestrictionType getRestriction() {
		return restriction;
	}

	public String getField() {
		return field;
	}

	EmbedType getEmbedType() {
		return embedType;
	}

	String getPreString() {
		return preString;
	}

	String getPostString() {
		return postString;
	}

	HqlFunctions getHqlFunction() {
		return hqlFunction;
	}

	boolean isEmptyAsNull() {
		return emptyAsNull;
	}

}
