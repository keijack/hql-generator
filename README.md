hql-generator
============

Generate hql from a POJO

As many of you have experienced, concatenating hql string is an annoying things when you using Hibernate. Of course, you can Criteria to do the query. But think of that, in some system, user want to input many fields to try to query the result, and sometimes they just want to query by the field they fill, but ignore the empty fields. Then you may write a lot of if phrase in your system.
 
However, here we provided this hql-generator, you can just define a POJO, add some Annotation to the POJO and its fields, then you can easily use HqlGenerator.generate(POJO) to get the hql and all the parameters you need to create a Hibernate Query object. See the flowing example.

This tool is suitable for you if
1. You have many hql strings that written in your java code, and mostly these hql strings are not joined table query.
2. You have to query data with combining AND conditions dynamically. 

It may be not suitable for you if
1. You have many table joins in your hql.
2. You want to make dynamic condition from page for user to choose.
3. You have complicated OR queries.

This project is now in Maven central repository, you can use following dependency in you pom.xml
```xml
<dependency>
	<groupId>org.keijack</groupId>
	<artifactId>hql-generator</artifactId>
	<version>1.0.0</version>
</dependency>
```

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
	
	@QueryCondition(field = "strValue", comparison = ComparisonType.LIKE, preString = "%", emptyAsNull = true)
	private String strValueEndWith;

	@QueryCondition(field = "strValue", comparison = ComparisonType.IN, emptyAsNull = true)
	private List<String> starValuesIn;

	@QueryCondition(field = "modelItems", comparison = ComparisonType.CONTAINS)
	private List<HibernateEntityItem> hasItems;

	@QueryCondition(field = "modelItems", comparison = ComparisonType.NOTCONTAINS)
	private HibernateEntityItem item;

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
QueryHibernateEntityParams queryParams = new QueryHibernateEntityParams();
// set the field you want to query
HqlAndParams hqlAndParams = HqlGenerator.generateHql(queryParams);
String hql = hqlAndParams.getHql();
Map<String, Object> params = hqlAndParams.getParams(); // the length equals the count of `?` in the above hql string
Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
for (Map.Entry<String, Object> entry : params.entrySet()) {
	query.setParameter(entry.getKey(), entry.getValue());
}

List<HibernateEntity> res = query.list();

```
