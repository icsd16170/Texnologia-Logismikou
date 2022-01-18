package project.errorhandling.validation.user.role;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import project.dto.UserDTO;

public class CustomerValidator implements
        ConstraintValidator<ValidCustomer, UserDTO> {

    private static final List<String> employeeTypes = Arrays.asList("SIMPLE", "CASHIER", "MANAGER");

    @Override
    public void initialize(ValidCustomer customer) {
    }

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if ("CUSTOMER".equals(userDTO.getRole())) {
            return Stream.of(validateAddress(userDTO.getAddress(), context),
                                 validateCardType(userDTO.getCardType(), context),
                                 validateCardNumber(userDTO.getCardNumber(), context),
                                 validateCvc(userDTO.getCvc(), context),
                                 validateExpirationDate(userDTO.getExpirationDate(), context))
                         .allMatch(result -> result);

        }
        return true;
    }

    private boolean validateExpirationDate(String expirationDate, ConstraintValidatorContext context) {
        if (expirationDate == null) {
            context.buildConstraintViolationWithTemplate("Expiration Date must not be null or empty")
                   .addPropertyNode("expirationDate")
                   .addConstraintViolation();
            return false;

        }
        return true;
    }

    private boolean validateCvc(String cvc, ConstraintValidatorContext context) {
        if (cvc == null) {
            context.buildConstraintViolationWithTemplate("Cvc must not be null or empty")
                   .addPropertyNode("cvc")
                   .addConstraintViolation();
            return false;

        }
        return true;

    }

    private boolean validateCardNumber(String cardNumber, ConstraintValidatorContext context) {
        if (cardNumber == null) {
            context.buildConstraintViolationWithTemplate("Card Number must not be null or empty")
                   .addPropertyNode("cardNumber")
                   .addConstraintViolation();
            return false;

        }
        return true;
    }

    private boolean validateAddress(String address, ConstraintValidatorContext context) {
        if (address == null) {
            context.buildConstraintViolationWithTemplate("Address must not be null or empty")
                   .addPropertyNode("address")
                   .addConstraintViolation();
            return false;

        }
        return true;
    }

    private boolean validateCardType(String cardType, ConstraintValidatorContext context) {
        if (cardType == null) {
            context.buildConstraintViolationWithTemplate("Card Type must not be null or empty")
                   .addPropertyNode("cardType")
                   .addConstraintViolation();
            return false;
        }
        return true;
    }
}