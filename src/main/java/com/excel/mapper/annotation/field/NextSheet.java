package com.excel.mapper.annotation.field;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
/**
 * Must be set to list field for the version
 */
public @interface NextSheet {
    /**
     * @return field name in class where this annotation uses
     */
    String select();

    /**
     * @return join field class name
     */
    String join();

    /**
     * @return join type
     */
    Class clazz();
}
