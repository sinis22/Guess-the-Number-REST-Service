package com.hs.guessthenumber.CONTROLLER;

import com.hs.guessthenumber.ENTITY.Error;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class GuessTheNumberControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String NULL_POINTER_MESSAGE = "Must include a guess.";
    private static final String EMPTY_RESULT_MESSAGE = "Could not find game. "
            + "Please ensure gameId is valid and try again.";


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public final ResponseEntity<Error> handleEmptyResultDataAccessException(){

        Error err = new Error();
        err.setMessage(EMPTY_RESULT_MESSAGE);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<Error> handleNullPointerException(){

        Error err = new Error();
        err.setMessage(NULL_POINTER_MESSAGE);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
