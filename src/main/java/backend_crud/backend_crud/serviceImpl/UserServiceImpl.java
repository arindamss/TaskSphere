package backend_crud.backend_crud.serviceImpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import backend_crud.backend_crud.constents.CrudConstents;
import backend_crud.backend_crud.entity.User;
import backend_crud.backend_crud.jwt.JWTHelper;
import backend_crud.backend_crud.jwt.UserDetailsServiceImpl;
import backend_crud.backend_crud.repository.UserRepository;
import backend_crud.backend_crud.service.UserService;
import backend_crud.backend_crud.utills.CrudUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl customUserDetails;

    @Autowired
    private JWTHelper jwtHelper;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try{
            if(validateSignup(requestMap)){
                User getUser=this.userRepo.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(getUser)){
                    this.userRepo.save(makeUser(requestMap));
                    return CrudUtils.getResponseEntity("Signup Successfully", HttpStatus.OK);
                }
                return CrudUtils.getResponseEntity("Email alredy exist", HttpStatus.BAD_REQUEST);
            }
            return CrudUtils.getResponseEntity(CrudConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public boolean validateSignup(Map<String, String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("email") && requestMap.containsKey("contactNumber") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }

    private User makeUser(Map<String, String> requestMap){
        User user=new User();
        user.setName(requestMap.get("name"));
        user.setEmail(requestMap.get("email"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setPassword(requestMap.get("password"));
        user.setRole("user");
        user.setStatus("true");
        return user;
    }

    
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
       try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

            if(auth.isAuthenticated()){
                if(customUserDetails.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<>("{\"token\":\""+jwtHelper.generateToken(customUserDetails.getUserDetails().getEmail(), 
                                customUserDetails.getUserDetails().getRole())+"\",\"message\":\"Login Successfull\"}", HttpStatus.OK); 
                }
                else{
                    return new ResponseEntity<>("{\"message\":\"Wait For Admin Approval\"}", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>("{\"message\":\"Wrong Crediential\"}", HttpStatus.BAD_REQUEST);
            }
       }
       catch (BadCredentialsException e) {
            log.error("Bad credentials error: {}", e.getMessage());
                return new ResponseEntity<String>("{\"message\":\"Bad Credentials.\"}", HttpStatus.BAD_REQUEST);
        } 
        catch (AuthenticationException e) {
            log.error("Authentication error: {}", e.getMessage());
            return new ResponseEntity<String>("{\"message\":\"Authentication error.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        } 
        catch (Exception e) {
            log.error("Unexpected error: {}", e);
            return new ResponseEntity<String>("{\"message\":\"Unexpected error occurred.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> checkLogin() {
        return CrudUtils.getResponseEntity("true", HttpStatus.OK);
    }

}
