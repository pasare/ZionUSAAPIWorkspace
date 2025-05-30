package org.zionusa.management.exception;


import org.springframework.http.HttpStatus;

public class ManagementServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus = null;

    ManagementServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public ManagementServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    ManagementServiceException(Throwable cause, HttpStatus httpStatus) {
        super(cause.getMessage(),cause);
        this.httpStatus = httpStatus;
    }

    ManagementServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagementServiceException(String message) {
        super(message);
    }

    ManagementServiceException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
