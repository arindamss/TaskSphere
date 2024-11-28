package backend_crud.backend_crud.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import backend_crud.backend_crud.constents.CrudConstents;
import backend_crud.backend_crud.rest.UserRest;
import backend_crud.backend_crud.service.UserService;
import backend_crud.backend_crud.utills.CrudUtils;

@RestController
@CrossOrigin(origins = "*")
public class UserRestImpl implements UserRest {

    @Autowired
    private UserService userService;
    
    @Override
    public ResponseEntity<String> signup(Map<String,String> requestMap){
        try{
            return userService.signup(requestMap);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkLogin() {
        try{
            return userService.checkLogin();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
