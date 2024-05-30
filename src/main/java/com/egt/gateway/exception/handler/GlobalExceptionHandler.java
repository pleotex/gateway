package com.egt.gateway.exception.handler;

import com.egt.gateway.exception.DuplicateRequestException;
import com.egt.gateway.exception.IllegalCurrencyException;
import com.egt.gateway.exception.IllegalPeriodException;
import com.egt.gateway.exception.IllegalXmlFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDetails> getErrorDetails(Exception e, HttpServletRequest request, HttpStatus status) {
        ErrorDetails details = new ErrorDetails(e, status, status.value(), request.getRequestURI());
        return new ResponseEntity<>(details, status);
    }

    @ExceptionHandler(value = DuplicateRequestException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateRequestException(DuplicateRequestException e, HttpServletRequest request){
        return getErrorDetails(e,request,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalPeriodException.class)
    public ResponseEntity<ErrorDetails> handleIllegalPeriodException(IllegalPeriodException e, HttpServletRequest request){
        return getErrorDetails(e,request,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = IllegalCurrencyException.class)
    public ResponseEntity<ErrorDetails> handleIllegalCurrencyException(IllegalCurrencyException e, HttpServletRequest request){
        return getErrorDetails(e,request,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalXmlFormatException.class)
    public ResponseEntity<ErrorDetails> handleIllegalXmlFormatException(IllegalXmlFormatException e, HttpServletRequest request){
        return getErrorDetails(e,request,HttpStatus.BAD_REQUEST);
    }
}
