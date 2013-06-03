package com.keijack.database.hibernate;

import junit.framework.TestCase;

import org.junit.Test;

import com.keijack.database.hibernate.QueryParamsValidator;

/**
 * 
 * @author Keijack
 * 
 */
public class ListTestModelCallValidatTest {
    /**
     * 测试检查类是否能正确执行
     */
    @Test
    public void testListCall() {
	QueryParamsValidator validator = new QueryParamsValidator();
	TestCase.assertTrue(validator.validate(ListTestModelCall.class));
    }
}
