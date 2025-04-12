package com.gymtutor.gymtutor.validation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMatchList {
    FieldMatch[] value();
}