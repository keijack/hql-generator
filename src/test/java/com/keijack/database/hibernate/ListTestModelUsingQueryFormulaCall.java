package com.keijack.database.hibernate;

import java.util.List;

import com.keijack.database.hibernate.stereotype.QueryFormula;
import com.keijack.database.hibernate.stereotype.QueryParamsFor;

@QueryParamsFor(value = TestModel.class, alias = "m")
public class ListTestModelUsingQueryFormulaCall {

    @QueryFormula(value = "m.parent is not null", appendValue = false)
    private Boolean notAppendValue;

    @QueryFormula("m.parent.id = ?")
    private Integer appendValue;

    @QueryFormula("m.parent.id = ? or m.parent.id = ? or m.parent.id = ?")
    private List<Integer> appendValueList;

    public Boolean getNotAppendValue() {
	return notAppendValue;
    }

    public void setNotAppendValue(Boolean notAppendValue) {
	this.notAppendValue = notAppendValue;
    }

    public Integer getAppendValue() {
	return appendValue;
    }

    public void setAppendValue(Integer appendValue) {
	this.appendValue = appendValue;
    }

    public List<Integer> getAppendValueList() {
	return appendValueList;
    }

    public void setAppendValueList(List<Integer> appendValueList) {
	this.appendValueList = appendValueList;
    }

}
