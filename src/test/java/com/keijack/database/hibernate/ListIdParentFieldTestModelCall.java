package com.keijack.database.hibernate;

import com.keijack.database.hibernate.stereotype.QueryParamsFor;

@QueryParamsFor(value = TestModel.class, fields = { "id", "parent" }, alias = "t", distinct = true)
public class ListIdParentFieldTestModelCall extends ListTestModelCall {

    @QueryParamsFor(value = TestModel.class, fields = "id", alias = "t", distinct = true)
    public static class ListIdFieldTestModelCall extends ListIdParentFieldTestModelCall {

    }
}
