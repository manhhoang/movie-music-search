package com.jd.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleControllerException(AppException appException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(appException.getErrorCode());
        errorResponse.setErrorMessage(appException.getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
}
