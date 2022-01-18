package project.errorhandling.validation.user.password;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PasswordChangeRequestValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChangePasswordRequest {
    String message() default "Password verification does not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}