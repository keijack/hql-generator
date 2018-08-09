package org.keijack.database.hibernate;

import java.util.List;

import org.keijack.database.hibernate.stereotype.RestrictionType;
import org.keijack.database.hibernate.stereotype.OrderBy;
import org.keijack.database.hibernate.stereotype.OrderByLevel;
import org.keijack.database.hibernate.stereotype.QueryCriterion;
import org.keijack.database.hibernate.stereotype.QueryParamsFor;
import org.keijack.database.hibernate.stereotype.SortOrder;

@QueryParamsFor(value = HibernateEntity.class, alias = "testModel")
public class QueryHibernateEntityParams extends ListTestModelCallParent {

	@QueryCriterion(field = "id", restriction = RestrictionType.EQUAL)
	private Integer id;

	@QueryCriterion(field = "strValue", restriction = RestrictionType.LIKE, postString = "%")
	private String strValueStartWith;
	/*
	 * 其中, 如果是 like 的话，prefix 和 suffix 中的 "%" 是根据Hql like 条件的内容来定的，因为传入的值是 null
	 * 的话，Hql 生成器不会生成这部分的条件，emptyAsNull 是把空字符串（空的Collection）也会当成 null
	 * 处理，否则就依然去数据库查询相关条件的内容
	 */
	@QueryCriterion(field = "strValue", restriction = RestrictionType.LIKE, preString = "%", emptyAsNull = true)
	private String strValueEndWith;

	@QueryCriterion(field = "strValue", restriction = RestrictionType.IN, emptyAsNull = true)
	private List<String> starValuesIn;

	/*
	 * 要相关的属性中全部包含所有的列表才算是真。
	 */
	@QueryCriterion(field = "modelItems", restriction = RestrictionType.CONTAINS)
	private List<HibernateEntityItem> hasItems;

	@QueryCriterion(field = "modelItems", restriction = RestrictionType.NOT_CONTAINS)
	private HibernateEntityItem item;

	@OrderBy(field = "id", orderBy = SortOrder.DESC)
	private OrderByLevel orderByIdDesc;

	/**
	 * order by strValue
	 */
	@OrderBy(field = "strValue", orderBy = SortOrder.ASC)
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

	public List<HibernateEntityItem> getHasItems() {
		return hasItems;
	}

	public void setHasItems(List<HibernateEntityItem> hasItems) {
		this.hasItems = hasItems;
	}

	public HibernateEntityItem getItem() {
		return item;
	}

	public void setItem(HibernateEntityItem item) {
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