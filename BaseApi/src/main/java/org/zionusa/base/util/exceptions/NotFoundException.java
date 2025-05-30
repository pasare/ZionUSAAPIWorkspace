package org.zionusa.base.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The entity could not be found in the system")
public class NotFoundException extends RuntimeException {

    public NotFoundException(){}

    public NotFoundException(String message){
        super(message);
    }
}
