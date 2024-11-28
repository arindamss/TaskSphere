package backend_crud.backend_crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend_crud.backend_crud.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    public List<Task> findByCreatedBy(String currentUser);

    public List<Task> findAssociateTasks(Integer userId);

    public Optional<Task> findTaskByUserAndId(String currentUser, Integer taskId);
    
}
