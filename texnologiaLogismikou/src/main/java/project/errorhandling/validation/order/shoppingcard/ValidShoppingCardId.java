package project.errorhandling.validation.order.shoppingcard;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ShoppingCardIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidShoppingCardId {
    String message() default "Shopping card does not exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}