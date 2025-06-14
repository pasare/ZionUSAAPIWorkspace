package org.zionusa.base.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "This operation is not allowed")
public class ForbiddenException extends RuntimeException{

    public ForbiddenException(){}

    public ForbiddenException(String message){
        super(message);
    }
}
