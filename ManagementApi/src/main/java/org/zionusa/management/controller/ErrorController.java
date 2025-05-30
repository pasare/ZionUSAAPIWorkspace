package org.zionusa.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zionusa.base.enums.EZoneId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorController {

    public static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler({BadCredentialsException.class})
    void handleUserNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "supplied credentials are invalid");
        log.error("401 the supplied credentials are invalid for user: {} ({})", request.getUserPrincipal().getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    @ExceptionHandler({AccessDeniedException.class})
    void handleAccessDenied(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value(), "Either the resource does not exists or the user is not permitted to access the resource");
        log.error("403 access is denied to the selected resource: {} for userId: {} ({})", request.getRequestURI(), request.getUserPrincipal().getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    @ExceptionHandler({ParseException.class})
    void handleParseException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Unable to parse input");
        log.error("500 there was an error parsing the data: {} supplied by user: {} ({})", request.getRequestURI(), request.getUserPrincipal().getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    void handleEmptyResultSet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "The requested resource was not found");
        log.error("404 the requested resource was not found on the system {} requested by user: {} ({})", request.getRequestURI(), request.getUserPrincipal().getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    //@ExceptionHandler(DataIntegrityViolationException)

    /*@ExceptionHandler({Exception.class})
    void handleGeneralException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Something went wrong with the server");
    }*/

}
