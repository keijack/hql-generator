package com.keijack.database.hibernate;

import com.keijack.database.hibernate.stereotype.QueryParamsFor;

@QueryParamsFor(value = TestModel.class, fields = { "id", "parent" }, alias = "t", distinct = true)
public class ListIdFieldTestModelCall extends ListTestModelCall {

}
