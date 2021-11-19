package com.karaoke.manager.validation.annotation.validator;

import com.google.common.collect.Sets;
import com.karaoke.manager.validation.annotation.StringContains;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class StringContainsValidator implements ConstraintValidator<StringContains, String> {
  private Set<String> AVAILABLE_STRINGS;

  @Override
  public void initialize(StringContains constraintAnnotation) {
    AVAILABLE_STRINGS = Sets.newHashSet(constraintAnnotation.listString());
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return s == null || AVAILABLE_STRINGS.contains(s);
  }
}
