package com.keijack.database.hibernate;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.keijack.database.hibernate.hqlconditions.OrderByLevel;

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
		    "from com.keijack.database.TestModel testModel",
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

    /**
     * elements 的情况
     */
    @Test
    public void testGetWhereExist() {
	ListTestModelCall listCall = new ListTestModelCall();
	TestModelItem testItem = new TestModelItem();
	listCall.setId(2);
	listCall.setItem(testItem);
	try {
	    HqlAndParams hql = HqlGenerator.generateHql(listCall);
	    TestCase.assertEquals(
		    "where 1 = 1 and (testModel.id) = ? and ? in elements (testModel.modelItems)",
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
		    "where 1 = 1 and (testModel.id) = ? and (1 = 0 or testModel.strValue = ? or testModel.strValue = ?) and (1 = 1 and testModel.parent.parentStrValue != ? and testModel.parent.parentStrValue != ?)",
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
		    "from com.keijack.database.TestModel testModel",
		    hql.getFrom());
	} catch (Exception e) {
	    e.printStackTrace();
	    TestCase.fail();
	}

    }

    @Test
    public void testSelectField() {
	ListIdFieldTestModelCall call = new ListIdFieldTestModelCall();
	try {
	    HqlAndParams hql = HqlGenerator.generateHql(call);
	    TestCase.assertEquals(
		    "select distinct t.id, t.parent from com.keijack.database.hibernate.TestModel t",
		    hql.getFrom());
	} catch (Exception e) {
	    e.printStackTrace();
	    TestCase.fail();
	}
    }
}
