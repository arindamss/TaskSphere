package backend_crud.backend_crud.utills;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CrudUtils {
    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus status){
        return new ResponseEntity("{\"message\":\""+message+"\"}", status);
    }
}
