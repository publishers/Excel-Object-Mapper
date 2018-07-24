package com.excel.mapper.annotation.field;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
/**
 * Use this annotation with Column in excel
 * */
public @interface Header {
    /**
     * Header name that will be checked.
     */
    String name();

    /**
     * Should this be required
     */
    boolean required() default true;

    /**
     * Column position in excel file starts from 0 to N
     */
    int position();
}
