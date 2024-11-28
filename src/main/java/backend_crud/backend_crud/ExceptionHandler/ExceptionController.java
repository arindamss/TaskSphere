package backend_crud.backend_crud.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundExceptionHandler(UserNotFoundException ex){
        ErrorMessage error=new ErrorMessage();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setErrorMessage("User Not Found");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
