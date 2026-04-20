package com.example.FakeCommerce.exceptions;

import com.example.FakeCommerce.utils.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<Void>>handleResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(APIResponse.error(ex.getMessage(),"Resource Not Found"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>>handleAllGenericExceptions(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.error("Something Went Wrong","Internal Server Error"));
    }
}
