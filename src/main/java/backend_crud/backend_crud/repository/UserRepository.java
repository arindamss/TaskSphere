package backend_crud.backend_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import backend_crud.backend_crud.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    public User findByEmailId(@Param("email") String email);

}
