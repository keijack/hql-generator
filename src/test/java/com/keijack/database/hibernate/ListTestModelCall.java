package com.keijack.database.hibernate;

import java.util.List;

import com.keijack.database.hibernate.stereotype.ConditionLogicType;
import com.keijack.database.hibernate.stereotype.OrderBy;
import com.keijack.database.hibernate.stereotype.OrderByLevel;
import com.keijack.database.hibernate.stereotype.OrderByType;
import com.keijack.database.hibernate.stereotype.QueryCondition;
import com.keijack.database.hibernate.stereotype.QueryParamsFor;

@QueryParamsFor(value = TestModel.class, alias = "testModel")
public class ListTestModelCall extends ListTestModelCallParent {

    @QueryCondition(field = "id", logicType = ConditionLogicType.equal)
    private Integer id;

    @QueryCondition(field = "strValue", logicType = ConditionLogicType.like, suffix = "%")
    private String strValueStartWith;
    /*
     * 其中, 如果是 like 的话，prefix 和 suffix 中的 "%" 是根据Hql like 条件的内容来定的，因为传入的值是 null
     * 的话，Hql 生成器不会生成这部分的条件，emptyAsNull 是把空字符串（空的Collection）也会当成 null
     * 处理，否则就依然去数据库查询相关条件的内容
     */
    @QueryCondition(field = "strValue", logicType = ConditionLogicType.like, prefix = "%", emptyAsNull = true)
    private String strValueEndWith;

    @QueryCondition(field = "strValue", logicType = ConditionLogicType.in, emptyAsNull = true)
    private List<String> starValuesIn;

    @QueryCondition(field = "modelItems", logicType = ConditionLogicType.notContains)
    private TestModelItem item;

    @OrderBy(field = "id", orderBy = OrderByType.desc)
    private OrderByLevel orderByIdDesc;

    /**
     * order by strValue
     */
    @OrderBy(field = "strValue", orderBy = OrderByType.asc)
    private OrderByLevel orderByStrValueAsc;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getStrValueStartWith() {
	return strValueStartWith;
    }

    public void setStrValueStartWith(String strValueStartWith) {
	this.strValueStartWith = strValueStartWith;
    }

    public List<String> getStarValuesIn() {
	return starValuesIn;
    }

    public void setStarValuesIn(List<String> starValuesIn) {
	this.starValuesIn = starValuesIn;
    }

    public TestModelItem getItem() {
	return item;
    }

    public void setItem(TestModelItem item) {
	this.item = item;
    }

    public OrderByLevel getOrderByIdDesc() {
	return orderByIdDesc;
    }

    public void setOrderByIdDesc(OrderByLevel orderByIdDesc) {
	this.orderByIdDesc = orderByIdDesc;
    }

    public OrderByLevel getOrderByStrValueAsc() {
	return orderByStrValueAsc;
    }

    public void setOrderByStrValueAsc(OrderByLevel orderByStrValueAsc) {
	this.orderByStrValueAsc = orderByStrValueAsc;
    }

    public String getStrValueEndWith() {
	return strValueEndWith;
    }

    public void setStrValueEndWith(String strValueEndWith) {
	this.strValueEndWith = strValueEndWith;
    }

}