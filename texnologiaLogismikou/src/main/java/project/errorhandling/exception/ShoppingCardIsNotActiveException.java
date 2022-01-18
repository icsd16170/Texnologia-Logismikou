package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ShoppingCardIsNotActiveException extends RuntimeException {
    public ShoppingCardIsNotActiveException(long id) {
        super(String.format("Shopping Card with id %s is not active", id));
    }
}
