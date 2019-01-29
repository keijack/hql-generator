package org.keijack.database.hibernate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.keijack.database.hibernate.ListIdParentFieldTestModelCall.ListIdFieldTestModelCall;
import org.keijack.database.hibernate.stereotype.OrderByLevel;

import junit.framework.TestCase;

/**
 * 测试
 * 
 * @author Keijack
 * 
 */
public class HqlGeneratorTest {

	/**
	 * 测试基本情况下获得的Hql
	 */
	@Test
	public void testGetHqlBasic() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		listCall.setId(1);
		listCall.setStrValueStartWith("abc");
		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals(
					"from org.keijack.database.hibernate.HibernateEntity testModel",
					hql.getFrom());
			TestCase.assertEquals(
					"where 1 = 1 and (testModel.id) = :id and (testModel.strValue) like :strValueStartWith",
					hql.getWhere());
			TestCase.assertEquals(2, hql.getParams().size());
			TestCase.assertEquals(Integer.valueOf(1), hql.getParams().get("id"));
			TestCase.assertEquals("abc%", hql.getParams().get("strValueStartWith"));
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	@Test
	public void testGetWhereExist() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		HibernateEntityItem testItem = new HibernateEntityItem();
		testItem.setItemId(1);
		HibernateEntityItem testItem2 = new HibernateEntityItem();
		testItem2.setItemId(2);
		List<HibernateEntityItem> items = new ArrayList<HibernateEntityItem>();
		items.add(testItem);
		items.add(testItem2);
		listCall.setHasItems(items);

		listCall.setId(2);

		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals(
					"where 1 = 1 and (testModel.id) = :id and (:hasItems0 in elements (testModel.modelItems) and :hasItems1 in elements (testModel.modelItems))",
					hql.getWhere());
			TestCase.assertEquals(Integer.valueOf(2), hql.getParams().get("id"));
			TestCase.assertEquals(testItem, hql.getParams().get("hasItems0"));
			TestCase.assertEquals(testItem2, hql.getParams().get("hasItems1"));
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	/**
	 * elements 的情况
	 */
	@Test
	public void testGetWhereNotExist() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		HibernateEntityItem testItem = new HibernateEntityItem();
		listCall.setId(2);
		listCall.setItem(testItem);
		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals(
					"where 1 = 1 and (testModel.id) = :id and (:item0 not in elements (testModel.modelItems))",
					hql.getWhere());
			TestCase.assertEquals(Integer.valueOf(2), hql.getParams().get("id"));
			TestCase.assertEquals(testItem, hql.getParams().get("item0"));
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	/**
	 * in 的测试
	 */
	@Test
	public void testGetWhereIn() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		listCall.setId(1);
		List<String> starValuesIn = new ArrayList<String>();
		starValuesIn.add("abcde");
		starValuesIn.add("keijack.test");
		listCall.setStarValuesIn(starValuesIn);
		List<String> pValuesNotIn = new ArrayList<String>();
		pValuesNotIn.add("qwerty");
		pValuesNotIn.add("poiuy");
		listCall.setNotInParentStrValue(pValuesNotIn);

		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals(
					"where 1 = 1 and (testModel.id) = :id and (1 = 0 or (testModel.strValue) = :starValuesIn0 or (testModel.strValue) = :starValuesIn1) "
							+ "and (1 = 1 and (testModel.parent.parentStrValue) != :notInParentStrValue0 and (testModel.parent.parentStrValue) != :notInParentStrValue1)",
					hql.getWhere());
			TestCase.assertEquals(Integer.valueOf(1), hql.getParams().get("id"));
			TestCase.assertEquals("abcde", hql.getParams().get("starValuesIn0"));
			TestCase.assertEquals("keijack.test", hql.getParams().get("starValuesIn1"));
			TestCase.assertEquals("qwerty", hql.getParams().get("notInParentStrValue0"));
			TestCase.assertEquals("poiuy", hql.getParams().get("notInParentStrValue1"));
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	/**
	 * null 测试
	 */
	@Test
	public void testGetWhereNull() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		listCall.setStrValueIsNull(true);
		listCall.setParentIdIsNull(false);

		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals(
					"where 1 = 1 and testModel.strValue is null and testModel.parent.parentId is not null",
					hql.getWhere());
			TestCase.assertEquals(0, hql.getParams().size());
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	/**
	 * 测试是否无视空值。
	 */
	@Test
	public void testWhereIgnoreNull() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		listCall.setStrValueStartWith("");
		listCall.setStarValuesIn(new ArrayList<String>());
		listCall.setStrValueEndWith("");

		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals(
					"where 1 = 1 and (testModel.strValue) like :strValueStartWith",
					hql.getWhere());
			TestCase.assertEquals(1, hql.getParams().size());
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}

	}

	/**
	 * orderBy的测试
	 */
	@Test
	public void testGetOrderBy() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		listCall.setOrderByIdDesc(OrderByLevel.LV2);
		listCall.setOrderByStrValueAsc(OrderByLevel.LV1);
		listCall.setOrderByParentId(OrderByLevel.LV2);
		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals(
					"order by testModel.strValue asc, testModel.id desc, testModel.parent.parentId asc",
					hql.getOrderBy());
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}

	}

	/**
	 * 测试日期
	 */
	@Test
	public void testSQLFunction() {
		QueryHibernateEntityParams listCall = new QueryHibernateEntityParams();
		listCall.setMonthOfDate(10);
		try {
			HqlAndParams hql = HqlGenerator.generateHql(listCall);
			TestCase.assertEquals("where 1 = 1 and month(testModel.date) = :monthOfDate",
					hql.getWhere());

		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	@Test
	public void testQueryParamsForAnnoInParent() {
		ListTestModelCallChild childCall = new ListTestModelCallChild();
		try {
			HqlAndParams hql = HqlGenerator.generateHql(childCall);
			TestCase.assertEquals(
					"from org.keijack.database.hibernate.HibernateEntity testModel",
					hql.getFrom());
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}

	}

	@Test
	public void testSelectOneField() {
		ListIdFieldTestModelCall call = new ListIdFieldTestModelCall();
		try {
			HqlAndParams hql = HqlGenerator.generateHql(call);
			TestCase.assertEquals(
					"select distinct t.id from org.keijack.database.hibernate.HibernateEntity t",
					hql.getFrom());
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	@Test
	public void testSelectField() {
		ListIdParentFieldTestModelCall call = new ListIdParentFieldTestModelCall();
		try {
			HqlAndParams hql = HqlGenerator.generateHql(call);
			TestCase.assertEquals(
					"select distinct t.id, t.parent from org.keijack.database.hibernate.HibernateEntity t",
					hql.getFrom());
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}
	}

	@Test
	public void testQueryListEmbed() {
		ListTestModelCallEmbed call = new ListTestModelCallEmbed();
		List<String> strs = new LinkedList<>();
		strs.add("aaaa");
		strs.add("bbb");
		call.setStrValueEndWithOr(strs);
		HqlAndParams hql = HqlGenerator.generateHql(call);
		TestCase.assertEquals(
				"from org.keijack.database.hibernate.HibernateEntity testModel where 1 = 1 and ((testModel.strValue) like :strValueEndWithOr0 or (testModel.strValue) like :strValueEndWithOr1) ",
				hql.getHql());
		TestCase.assertEquals("%aaaa", hql.getParams().get("strValueEndWithOr0"));
		TestCase.assertEquals("%bbb", hql.getParams().get("strValueEndWithOr1"));
	}
}
