package project.errorhandling.validation.order.status;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import project.service.Constants;

public class OrderStatusValidator implements
        ConstraintValidator<ValidOrderStatus, String> {

    private static final List<String> validStatuses =
            List.of(Constants.CANCELLED, Constants.CREATED, Constants.PENDING, Constants.COMPLETED, Constants.CHARGED);

    @Override
    public void initialize(ValidOrderStatus status) {
    }

    @Override
    public boolean isValid(String status,
            ConstraintValidatorContext cxt) {
        return validStatuses.contains(status);
    }

}