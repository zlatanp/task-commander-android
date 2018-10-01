package task.commander.exceptions.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import task.commander.exceptions.BadRequestException;
import task.commander.exceptions.NotFoundException;

public class ExceptionResolver {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity badRequestException(HttpServletRequest request, BadRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundException(HttpServletRequest request, NotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity dataAccessException(HttpServletRequest request, DataAccessException exception) {
        return new ResponseEntity<>("{\"message\": \"" + exception.getMessage() + "\"}", HttpStatus.CONFLICT);
    }
}