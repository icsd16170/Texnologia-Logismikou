package project.errorhandling.validation.user.role;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = EmployeeValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmployee {
    String message() default "Invalid employeeType. Should be one of : SIMPLE,CASHIER,MANAGER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}