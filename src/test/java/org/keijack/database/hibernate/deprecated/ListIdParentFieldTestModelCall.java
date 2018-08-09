package org.keijack.database.hibernate.deprecated;

import org.keijack.database.hibernate.stereotype.QueryParamsFor;

@QueryParamsFor(value = HibernateEntity.class, fields = { "id", "parent" }, alias = "t", distinct = true)
public class ListIdParentFieldTestModelCall extends ListTestModelCall {

    @QueryParamsFor(value = HibernateEntity.class, fields = "id", alias = "t", distinct = true)
    public static class ListIdFieldTestModelCall extends ListIdParentFieldTestModelCall {

    }
}
