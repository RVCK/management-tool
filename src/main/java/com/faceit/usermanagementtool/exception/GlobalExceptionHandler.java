package com.faceit.usermanagementtool.exception;

import io.grpc.StatusRuntimeException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import com.faceit.usermanagementtool.openapi.model.Error;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Error> handleDuplicateKeyException() {
        return buildErrorResponse("Duplicated Key", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> handleResponseStatusException(ResponseStatusException ex) {
        return buildErrorResponse(ex.getReason(), (HttpStatus) ex.getStatusCode());
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Error> handleGrpcException(StatusRuntimeException ex) {
        //GRPC onError()
        ResponseEntity<Error> error = null;
        switch (ex.getStatus().getCode()){
            case ALREADY_EXISTS -> error = buildErrorResponse("Duplicated Key in MongoDB: " + ex.getMessage(), HttpStatus.CONFLICT);
            case NOT_FOUND -> error = buildErrorResponse("Not Found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
            case CANCELLED -> error = buildErrorResponse("Cancelled stream: " + ex.getMessage(), HttpStatus.GONE);
            default -> error = buildErrorResponse("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }        
        return error;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleAllUncaughtException(Exception ex) {
        return buildErrorResponse("Unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Error> buildErrorResponse(final String message, final HttpStatus httpStatus) {
        Error error = new Error();
        error.setDetail(message);
        error.setCode(httpStatus.value());
        error.setTitle(httpStatus.name().toUpperCase());
        return new ResponseEntity<>(error, httpStatus);
    }
}
