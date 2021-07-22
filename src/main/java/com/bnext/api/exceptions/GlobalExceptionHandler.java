package com.bnext.api.exceptions;

import com.bnext.api.exceptions.dtos.ErrorDetailsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetailsDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<?> invalidDataException(InvalidDataException ex, WebRequest request) {
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetailsDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetailsDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}