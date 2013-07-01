package com.keijack.database.hibernate.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Keijack
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryFormula {

    /**
     * The formula string.
     * 
     * @return
     */
    String value();

    /**
     * Most situation, the the field's value will append to the hql. However,
     * when this is set to false, the value won't append to the hql.
     * 
     * @return
     */
    boolean appendValue() default true;

}
