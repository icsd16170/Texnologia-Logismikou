package project.errorhandling.validation.order.customername;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import project.service.user.UserService;

public class CustomerUserNameValidator implements
        ConstraintValidator<ValidCustomerUserName, String> {

    private final UserService userService;

    public CustomerUserNameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(ValidCustomerUserName customerUserName) {
    }

    @Override
    public boolean isValid(String customerUserName,
            ConstraintValidatorContext cxt) {
        return userService.existsByUserName(customerUserName);
    }

}