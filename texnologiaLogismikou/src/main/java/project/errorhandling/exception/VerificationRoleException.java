package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class VerificationRoleException extends RuntimeException {

    public VerificationRoleException() {
        super("You are not authorized to perform this action");
    }

}
