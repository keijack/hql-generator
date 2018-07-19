package org.keijack.database.hibernate;

import junit.framework.TestCase;

import org.junit.Test;
import org.keijack.database.hibernate.QueryParamsValidator;

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
	TestCase.assertTrue(validator.validate(ListIdParentFieldTestModelCall.class));
	TestCase.assertTrue(validator.validate(ListTestModelCallChild.class));
    }
}
