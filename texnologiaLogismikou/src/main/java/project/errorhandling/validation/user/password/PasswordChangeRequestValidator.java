package project.errorhandling.validation.user.password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import project.dto.ChangePasswordRequestDTO;

public class PasswordChangeRequestValidator implements
        ConstraintValidator<ValidChangePasswordRequest, ChangePasswordRequestDTO> {

    @Override
    public void initialize(ValidChangePasswordRequest customer) {
    }

    @Override
    public boolean isValid(ChangePasswordRequestDTO userDTO, ConstraintValidatorContext context) {
        return userDTO.getNewPassword().equals(userDTO.getNewPasswordVerification());
    }

}