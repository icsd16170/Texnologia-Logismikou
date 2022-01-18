package project.errorhandling.validation.user.password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements
        ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword contactNumber) {
    }

    @Override
    public boolean isValid(String passwordField,
            ConstraintValidatorContext cxt) {
        return passwordField.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$");
    }

}