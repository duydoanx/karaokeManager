package com.karaoke.manager.validation.annotation.validator;

import com.karaoke.manager.validation.annotation.EnumContains;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumContainsStringValidator implements ConstraintValidator<EnumContains, String> {
  private Set<String> AVAILABLE_ENUM_NAMES;

  @Override
  public void initialize(EnumContains constraintAnnotation) {
    Class<? extends Enum<?>> enumSelected = constraintAnnotation.enumClass();
    AVAILABLE_ENUM_NAMES =
        Arrays.stream(enumSelected.getEnumConstants()).map(Enum::name).collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return s == null || AVAILABLE_ENUM_NAMES.contains(s);
  }
}
