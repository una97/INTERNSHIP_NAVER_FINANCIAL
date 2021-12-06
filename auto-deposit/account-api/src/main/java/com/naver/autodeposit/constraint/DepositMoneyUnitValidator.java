package com.naver.autodeposit.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DepositMoneyUnitValidator implements ConstraintValidator<DepositMoneyUnit, Long> {
    @Override
    public void initialize(DepositMoneyUnit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long money, ConstraintValidatorContext context) {
        return money > 0 && money % 10000 == 0;
    }

}
