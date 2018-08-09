package org.keijack.database.hibernate;

import java.util.List;

import org.keijack.database.hibernate.stereotype.RestrictionType;
import org.keijack.database.hibernate.stereotype.HqlFunctions;
import org.keijack.database.hibernate.stereotype.OrderBy;
import org.keijack.database.hibernate.stereotype.OrderByLevel;
import org.keijack.database.hibernate.stereotype.QueryCriterion;
import org.keijack.database.hibernate.stereotype.SortOrder;

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
    @QueryCriterion(field = "parent.parentStrValue", restriction = RestrictionType.NOT_IN)
    private List<String> notInParentStrValue;

    /**
     * strValues是否为空
     */
    @QueryCriterion(field = "strValue", restriction = RestrictionType.IS_NULL)
    private Boolean strValueIsNull;

    /**
     * ParentId 是否为空
     */
    @QueryCriterion(field = "parent.parentId", restriction = RestrictionType.IS_NULL)
    private Boolean parentIdIsNull;

    /**
     * 获得日期
     */
    @QueryCriterion(field = "date", restriction = RestrictionType.EQUAL, hqlFunction = HqlFunctions.MONTH)
    private Integer monthOfDate;

    /**
     * order by parentId
     */
    @OrderBy(field = "parent.parentId", orderBy = SortOrder.ASC)
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
