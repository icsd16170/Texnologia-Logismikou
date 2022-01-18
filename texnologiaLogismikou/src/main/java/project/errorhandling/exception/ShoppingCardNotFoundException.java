package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ShoppingCardNotFoundException extends RuntimeException {
    public ShoppingCardNotFoundException(long id) {
        super(String.format("Shopping Card with id %s not found", id));
    }

    public ShoppingCardNotFoundException(String customerUserName) {
        super(String.format("Shopping Card for customerUserName %s not found", customerUserName));
    }
}
