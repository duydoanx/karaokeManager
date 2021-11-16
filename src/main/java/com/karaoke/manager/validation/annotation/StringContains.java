package com.karaoke.manager.validation.annotation;

import com.karaoke.manager.validation.annotation.validator.StringContainsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringContainsValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringContains {
  String message() default "Invalid string.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] listString() default {};
}
