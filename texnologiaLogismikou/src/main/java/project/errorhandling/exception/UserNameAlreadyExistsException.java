package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNameAlreadyExistsException extends RuntimeException {

    public UserNameAlreadyExistsException(String userName) {
        super(String.format("User with userName %s already exists", userName));
    }

}
