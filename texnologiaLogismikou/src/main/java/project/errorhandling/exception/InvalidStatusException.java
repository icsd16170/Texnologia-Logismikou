package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String invalidStatus, String expectedStatuses, String objectName) {
        super(String.format("Invalid Status. Only %s with status %s can change to %s ", objectName, expectedStatuses, invalidStatus));
    }

    public InvalidStatusException() {
        super("Invalid Status. Only shopping card with status active can change ");
    }
}
