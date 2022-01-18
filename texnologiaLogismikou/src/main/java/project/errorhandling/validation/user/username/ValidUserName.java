package project.errorhandling.validation.user.username;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UserNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserName {
    String message() default "Username should contain only letters, numbers or _ , start with a letter and be at least 5 characters long";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}