package project.errorhandling.validation.user.role;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleValidator implements
        ConstraintValidator<ValidRole, String> {

    private static final List<String> roles = Arrays.asList("ADMIN","EMPLOYEE","CUSTOMER");

    @Override
    public void initialize(ValidRole contactNumber) {
    }

    @Override
    public boolean isValid(String role,
            ConstraintValidatorContext cxt) {
        return roles.contains(role);
    }

}