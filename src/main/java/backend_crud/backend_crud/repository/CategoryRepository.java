package backend_crud.backend_crud.repository;

import backend_crud.backend_crud.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> getAllCategory(@Param("username") String username);

    @Query("select c from Category c where c.createdBy=:username and c.id=:id")
    Optional<Category> getCategory(@Param("username") String username, @Param("id") Integer id);

}
