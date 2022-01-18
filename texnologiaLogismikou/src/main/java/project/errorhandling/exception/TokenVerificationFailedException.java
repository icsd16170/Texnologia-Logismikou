package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TokenVerificationFailedException extends RuntimeException {
    public TokenVerificationFailedException(String message) {
        super(message);
    }

}
