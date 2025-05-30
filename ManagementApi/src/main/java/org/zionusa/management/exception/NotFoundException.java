package org.zionusa.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends ManagementServiceException {

    public NotFoundException(){super("The Requested Item Was Not Found", HttpStatus.NOT_FOUND);}

    public NotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND);
    }

}
