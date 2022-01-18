package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidQuantiyException extends RuntimeException {
    public InvalidQuantiyException(Long id) {
        super(String.format("Insufficient Quantity for DVD with id %s ", id));
    }

}
