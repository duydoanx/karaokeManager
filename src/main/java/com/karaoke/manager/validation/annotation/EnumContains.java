package com.karaoke.manager.validation.annotation;

import com.karaoke.manager.validation.annotation.validator.EnumContainsEnumValidator;
import com.karaoke.manager.validation.annotation.validator.EnumContainsStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EnumContainsStringValidator.class, EnumContainsEnumValidator.class})
@Target({
  ElementType.FIELD,
  ElementType.METHOD,
  ElementType.ANNOTATION_TYPE,
  ElementType.PARAMETER,
  ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumContains {

  String message() default "Invalid.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends Enum<?>> enumClass();
}
