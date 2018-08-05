hql-generator
============

Generate hql from a parameter bean

In my early projects, we wrote many hql string in codes, these hqls make many problems. First, it's hard to test these hqls; Secondary, you maybe write many if/else to make the combine conditions. So this project finally came out. It can generate hql from a bean with given annotations, so the IDE can check if your condition is right or not. 
And I even offer a call validator for you to test your QueryParamsCall.
You may use this tool in following condition:
1. You have many hql that written in java code, and most of them comes form one table.
2. You have to query data with combining AND conditions dynamically. 

It not suitable for you when
1. You have many table joins in your hql.
2. You want to make dynamic condition from page for user to choose.
3. You have OR query.

The following is an example

```java
// The query condition bean:

// imports

@QueryParamsFor(value = HibernateEntity.class, alias = "m")
public class QueryHibernateEntityParams extends ListTestModelCallParent {

	@QueryCondition(field = "id", comparison = ComparisonType.EQUAL)
	private Integer id;

	@QueryCondition(field = "strValue", comparison = ComparisonType.LIKE, postString = "%")
	private String strValueStartWith;
	/*
	 * 其中, 如果是 like 的话，prefix 和 suffix 中的 "%" 是根据Hql like 条件的内容来定的，因为传入的值是 null
	 * 的话，Hql 生成器不会生成这部分的条件，emptyAsNull 是把空字符串（空的Collection）也会当成 null
	 * 处理，否则就依然去数据库查询相关条件的内容
	 */
	@QueryCondition(field = "strValue", comparison = ComparisonType.LIKE, preString = "%", emptyAsNull = true)
	private String strValueEndWith;

	@QueryCondition(field = "strValue", comparison = ComparisonType.IN, emptyAsNull = true)
	private List<String> starValuesIn;

	/*
	 * 要相关的属性中全部包含所有的列表才算是真。
	 */
	@QueryCondition(field = "modelItems", comparison = ComparisonType.CONTAINS)
	private List<HibernateEntityItem> hasItems;

	@QueryCondition(field = "modelItems", comparison = ComparisonType.NOTCONTAINS)
	private HibernateEntityItem item;
	
	@QueryFormula(value = "m.parent is not null", appendValue = false)
    private Boolean notAppendValue;

    @QueryFormula("m.parent.id = ?")
    private Integer appendValue;

    @QueryFormula("m.parent.id = ? or m.parent.id = ? or m.parent.id = ?")
    private List<Integer> appendValueList;

	@OrderBy(field = "id", orderBy = SortOrder.DESC)
	private OrderByLevel orderByIdDesc;

	/**
	 * order by strValue
	 */
	@OrderBy(field = "strValue", orderBy = SortOrder.ASC)
	private OrderByLevel orderByStrValueAsc;

	// getter/setter
}


```

Then you can generate the hql using:
```java
QueryHibernateEntityParams params = new QueryHibernateEntityParams();
// set the field you want to query
HqlAndParams hql = HqlGenerator.generateHql(params);
String hql = hql.getHql();
Object[] queryParams = hql.getParams(); // the length equals the count of `?` in the above hql string

```