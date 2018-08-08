package org.keijack.database.hibernate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.keijack.database.hibernate.HqlAndParams;
import org.keijack.database.hibernate.HqlGeneratException;
import org.keijack.database.hibernate.HqlGenerator;
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
	ListTestModelCall listCall = new ListTestModelCall();
	listCall.setId(1);
	listCall.setStrValueStartWith("abc");
	try {
	    HqlAndParams hql = HqlGenerator.generateHql(listCall);
	    TestCase.assertEquals(
		    "from org.keijack.database.hibernate.HibernateEntity testModel",
		    hql.getFrom());
	    TestCase.assertEquals(
		    "where 1 = 1 and (testModel.id) = ? and (testModel.strValue) like ?",
		    hql.getWhere());
	    TestCase.assertEquals(2, hql.getParams().length);
	    TestCase.assertEquals(Integer.valueOf(1), hql.getParams()[0]);
	    TestCase.assertEquals("abc%", hql.getParams()[1]);
	} catch (Exception e) {
	    e.printStackTrace();
	    TestCase.fail();
	}
    }

    @Test
    public void testGetWhereExist() {
	ListTestModelCall listCall = new ListTestModelCall();
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
		    "where 1 = 1 and (testModel.id) = ? and (? in elements (testModel.modelItems) and ? in elements (testModel.modelItems))",
		    hql.getWhere());
	    TestCase.assertEquals(Integer.valueOf(2), hql.getParams()[0]);
	    TestCase.assertEquals(testItem, hql.getParams()[1]);
	    TestCase.assertEquals(testItem2, hql.getParams()[2]);
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
	ListTestModelCall listCall = new ListTestModelCall();
	HibernateEntityItem testItem = new HibernateEntityItem();
	listCall.setId(2);
	listCall.setItem(testItem);
	try {
	    HqlAndParams hql = HqlGenerator.generateHql(listCall);
	    TestCase.assertEquals(
		    "where 1 = 1 and (testModel.id) = ? and (? not in elements (testModel.modelItems))",
		    hql.getWhere());
	    TestCase.assertEquals(Integer.valueOf(2), hql.getParams()[0]);
	    TestCase.assertEquals(testItem, hql.getParams()[1]);
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
	ListTestModelCall listCall = new ListTestModelCall();
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
		    "where 1 = 1 and (testModel.id) = ? and (1 = 0 or (testModel.strValue) = ? or (testModel.strValue) = ?) "
			    + "and (1 = 1 and (testModel.parent.parentStrValue) != ? and (testModel.parent.parentStrValue) != ?)",
		    hql.getWhere());
	    TestCase.assertEquals(Integer.valueOf(1), hql.getParams()[0]);
	    TestCase.assertEquals("abcde", hql.getParams()[1]);
	    TestCase.assertEquals("keijack.test", hql.getParams()[2]);
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
	ListTestModelCall listCall = new ListTestModelCall();
	listCall.setStrValueIsNull(true);
	listCall.setParentIdIsNull(false);

	try {
	    HqlAndParams hql = HqlGenerator.generateHql(listCall);
	    TestCase.assertEquals(
		    "where 1 = 1 and testModel.strValue is null and testModel.parent.parentId is not null",
		    hql.getWhere());
	    TestCase.assertEquals(0, hql.getParams().length);
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
	ListTestModelCall listCall = new ListTestModelCall();
	listCall.setStrValueStartWith("");
	listCall.setStarValuesIn(new ArrayList<String>());
	listCall.setStrValueEndWith("");

	try {
	    HqlAndParams hql = HqlGenerator.generateHql(listCall);
	    TestCase.assertEquals(
		    "where 1 = 1 and (testModel.strValue) like ?",
		    hql.getWhere());
	    TestCase.assertEquals(1, hql.getParams().length);
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
	ListTestModelCall listCall = new ListTestModelCall();
	listCall.setOrderByIdDesc(OrderByLevel.level2);
	listCall.setOrderByStrValueAsc(OrderByLevel.level1);
	listCall.setOrderByParentId(OrderByLevel.level2);
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
	ListTestModelCall listCall = new ListTestModelCall();
	listCall.setMonthOfDate(10);
	try {
	    HqlAndParams hql = HqlGenerator.generateHql(listCall);
	    TestCase.assertEquals("where 1 = 1 and month(testModel.date) = ?",
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
    public void testQueryFormulaField() {
	ListTestModelUsingQueryFormulaCall call = new ListTestModelUsingQueryFormulaCall();
	call.setNotAppendValue(true);
	call.setAppendValue(1);
	List<Integer> ids = new ArrayList<Integer>();
	ids.add(1);
	ids.add(2);
	ids.add(3);
	call.setAppendValueList(ids);
	try {
	    HqlAndParams hql = HqlGenerator.generateHql(call);
	    TestCase.assertEquals(
		    "where 1 = 1 and (m.parent is not null) and (m.parent.id = ?) and (m.parent.id = ? or m.parent.id = ? or m.parent.id = ?)",
		    hql.getWhere());
	    TestCase.assertEquals(4, hql.getParams().length);
	    TestCase.assertEquals(1, hql.getParams()[0]);
	    TestCase.assertEquals(1, hql.getParams()[1]);
	    TestCase.assertEquals(2, hql.getParams()[2]);
	    TestCase.assertEquals(3, hql.getParams()[3]);
	} catch (Exception e) {
	    e.printStackTrace();
	    TestCase.fail();
	}
    }

    @Test
    public void testQueryListFormulaField() {
	ListTestModelUsingQueryFormulaCall call = new ListTestModelUsingQueryFormulaCall();
	List<Integer> ids = new ArrayList<Integer>();
	ids.add(1);
	ids.add(2);
	ids.add(3);
	ids.add(4);
	call.setAppendValueList(ids);
	try {
	    HqlAndParams hql = HqlGenerator.generateHql(call);
	    TestCase.assertEquals(
		    "where 1 = 1 and (m.parent.id = ? or m.parent.id = ? or m.parent.id = ?)",
		    hql.getWhere());
	    TestCase.assertEquals(3, hql.getParams().length);
	    TestCase.assertEquals(1, hql.getParams()[0]);
	    TestCase.assertEquals(2, hql.getParams()[1]);
	    TestCase.assertEquals(3, hql.getParams()[2]);
	} catch (Exception e) {
	    e.printStackTrace();
	    TestCase.fail();
	}
    }

    @Test
    public void testQueryListFormulaFieldError() {
	ListTestModelUsingQueryFormulaCall call = new ListTestModelUsingQueryFormulaCall();
	List<Integer> ids = new ArrayList<Integer>();
	ids.add(1);
	ids.add(2);
	call.setAppendValueList(ids);
	try {
	    HqlGenerator.generateHql(call);
	    TestCase.fail();
	} catch (HqlGeneratException e) {
	    TestCase.assertEquals(
		    "Not enough parameters in Query parameter bean's field [appendValueList].",
		    e.getMessage());
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
		"from org.keijack.database.hibernate.HibernateEntity testModel where 1 = 1 and ((testModel.strValue) like ? or (testModel.strValue) like ?) ",
		hql.getHql());
	TestCase.assertEquals("%aaaa", hql.getParams()[0]);
	TestCase.assertEquals("%bbb", hql.getParams()[1]);
    }
}
