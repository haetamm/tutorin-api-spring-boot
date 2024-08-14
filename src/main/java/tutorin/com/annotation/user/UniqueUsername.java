package tutorin.com.annotation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import tutorin.com.validation.user.UniqueEmailValidator;
import tutorin.com.validation.user.UniqueUsernameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "Username has already been taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
