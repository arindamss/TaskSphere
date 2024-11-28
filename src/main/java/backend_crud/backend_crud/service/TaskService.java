package backend_crud.backend_crud.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import backend_crud.backend_crud.ExceptionHandler.TaskNotFoundException;
import backend_crud.backend_crud.ExceptionHandler.UserNotFoundException;
import backend_crud.backend_crud.dto.RelatedTaskDTO;
import backend_crud.backend_crud.dto.TaskAssociateDto;
import backend_crud.backend_crud.dto.TaskDTO;

public interface TaskService {
    public ResponseEntity<String> addTask(backend_crud.backend_crud.dto.TaskReqDTO taskReqDto) throws UserNotFoundException;

    public ResponseEntity<String> mapUser(TaskAssociateDto associateDto);

    public ResponseEntity<List<TaskDTO>> getAllTask();

    public ResponseEntity<String> deleteAssociate(Integer taskId, Integer userId);

    public ResponseEntity<String> setSubTask(Integer parentTaskId, Integer subTaskId);

    public ResponseEntity<String> addSubTask(Integer parentTaskId, TaskDTO taskDto);

    public ResponseEntity<List<TaskDTO>> getAssociateTask();

    public ResponseEntity<String> setSubTask(RelatedTaskDTO relatedTaskDTO) throws TaskNotFoundException;

    public ResponseEntity<Set<TaskDTO>> getSubTask(Integer taskId);
}
