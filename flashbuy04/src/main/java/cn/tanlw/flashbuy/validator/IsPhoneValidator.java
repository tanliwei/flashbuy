package cn.tanlw.flashbuy.validator;

import cn.tanlw.flashbuy.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsPhoneValidator implements ConstraintValidator<IsPhone, String> {
    private boolean required = false;
    @Override
    public void initialize(IsPhone constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required){
            return ValidatorUtil.isPhone(value);
        }
        return StringUtils.isEmpty(value) || ValidatorUtil.isPhone(value);
    }
}
