package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordChangedException extends RuntimeException {

    public PasswordChangedException() {
        super("Password cannot be changed");
    }

}
