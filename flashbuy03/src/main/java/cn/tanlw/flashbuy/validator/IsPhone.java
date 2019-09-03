package cn.tanlw.flashbuy.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,
ElementType.FIELD,
ElementType.ANNOTATION_TYPE,
ElementType.CONSTRUCTOR,
ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsPhoneValidator.class})
public @interface IsPhone {
    boolean required() default true;
    String message() default "the phone number format is wrong";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
    
}
