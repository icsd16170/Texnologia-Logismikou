package project.errorhandling.validation.user.username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameValidator implements
        ConstraintValidator<ValidUserName, String> {

    @Override
    public void initialize(ValidUserName contactNumber) {
    }

    @Override
    public boolean isValid(String userNameField,
            ConstraintValidatorContext cxt) {
        return userNameField != null && userNameField.matches("^[A-Za-z][a-zA-Z0-9_]*$") && (userNameField.length() >= 5);
    }

}