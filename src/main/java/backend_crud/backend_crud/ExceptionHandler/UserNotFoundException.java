package backend_crud.backend_crud.ExceptionHandler;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String msg){
        super(msg);
    }
}
