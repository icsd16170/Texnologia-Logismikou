package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DVDNotFoundException extends RuntimeException {
    public DVDNotFoundException(Long dvdId) {
        super(String.format("DVD with dvdId %s not found", dvdId));
    }
}
