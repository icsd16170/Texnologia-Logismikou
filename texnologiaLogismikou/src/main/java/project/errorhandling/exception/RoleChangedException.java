package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RoleChangedException extends RuntimeException {

    public RoleChangedException() {
        super("Role cannot be changed");
    }

}
