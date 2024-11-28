package backend_crud.backend_crud.ExceptionHandler;

import lombok.Data;

@Data
public class ErrorMessage {
    private int errorCode;
    private String errorMessage;
}
