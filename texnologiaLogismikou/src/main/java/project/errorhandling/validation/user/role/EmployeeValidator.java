package project.errorhandling.validation.user.role;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import project.dto.UserDTO;

public class EmployeeValidator implements
        ConstraintValidator<ValidEmployee, UserDTO> {

    private static final List<String> employeeTypes = Arrays.asList("SIMPLE", "CASHIER", "MANAGER");

    @Override
    public void initialize(ValidEmployee employee) {
    }

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
        if ("EMPLOYEE".equals(userDTO.getRole())) {
            if (userDTO.getEmployeeType() != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid Employee Type. Should be one of : SIMPLE,CASHIER,MANAGER")
                       .addPropertyNode("employeeType")
                       .addConstraintViolation();
                return employeeTypes.contains(userDTO.getEmployeeType());
            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("employeeType must not be null")
                       .addPropertyNode("employeeType")
                       .addConstraintViolation();
                return false;
            }
        }
        return true;
    }

}