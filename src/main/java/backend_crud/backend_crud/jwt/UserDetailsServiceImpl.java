package backend_crud.backend_crud.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import backend_crud.backend_crud.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    private backend_crud.backend_crud.entity.User userDetails;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside UserDetails {}", userDetails);

        userDetails=userRepo.findByEmailId(username);
        if(userDetails == null){
            throw new UsernameNotFoundException("User is Not Found");
        }
        else{
            return new org.springframework.security.core.userdetails.User(userDetails.getEmail(), userDetails.getPassword(), new ArrayList<>()); 
        }
    }

    public backend_crud.backend_crud.entity.User getUserDetails(){
        return userDetails;
    } 
    
}
