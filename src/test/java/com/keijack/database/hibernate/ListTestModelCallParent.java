package com.keijack.database.hibernate;

import java.util.List;

import com.keijack.database.hibernate.stereotype.ConditionLogicType;
import com.keijack.database.hibernate.stereotype.OrderBy;
import com.keijack.database.hibernate.stereotype.OrderByLevel;
import com.keijack.database.hibernate.stereotype.OrderByType;
import com.keijack.database.hibernate.stereotype.QueryCondition;
import com.keijack.database.hibernate.stereotype.SqlFunctions;

/**
 * 支持父类继承
 * 
 * @author Keijack
 * 
 */
public class ListTestModelCallParent {
    /**
     * order by parentStrValue
     */
    @QueryCondition(field = "parent.parentStrValue", logicType = ConditionLogicType.notIn)
    private List<String> notInParentStrValue;

    /**
     * strValues是否为空
     */
    @QueryCondition(field = "strValue", logicType = ConditionLogicType.isNull)
    private Boolean strValueIsNull;

    /**
     * ParentId 是否为空
     */
    @QueryCondition(field = "parent.parentId", logicType = ConditionLogicType.isNull)
    private Boolean parentIdIsNull;

    /**
     * 获得日期
     */
    @QueryCondition(field = "date", logicType = ConditionLogicType.equal, sqlFunction = SqlFunctions.month)
    private Integer monthOfDate;

    /**
     * order by parentId
     */
    @OrderBy(field = "parent.parentId", orderBy = OrderByType.asc)
    private OrderByLevel orderByParentId;

    public List<String> getNotInParentStrValue() {
	return notInParentStrValue;
    }

    public void setNotInParentStrValue(List<String> notInParentStrValue) {
	this.notInParentStrValue = notInParentStrValue;
    }

    public Boolean getStrValueIsNull() {
	return strValueIsNull;
    }

    public void setStrValueIsNull(Boolean strValueIsNull) {
	this.strValueIsNull = strValueIsNull;
    }

    public Boolean getParentIdIsNull() {
	return parentIdIsNull;
    }

    public void setParentIdIsNull(Boolean parentIdIsNull) {
	this.parentIdIsNull = parentIdIsNull;
    }

    public Integer getMonthOfDate() {
	return monthOfDate;
    }

    public void setMonthOfDate(Integer monthOfDate) {
	this.monthOfDate = monthOfDate;
    }

    public OrderByLevel getOrderByParentId() {
	return orderByParentId;
    }

    public void setOrderByParentId(OrderByLevel orderByParentId) {
	this.orderByParentId = orderByParentId;
    }
}
