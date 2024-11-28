package backend_crud.backend_crud.ExceptionHandler;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String msg){
        super(msg);
    }
}
