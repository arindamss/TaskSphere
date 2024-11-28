package backend_crud.backend_crud.rest;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import backend_crud.backend_crud.ExceptionHandler.UserNotFoundException;
import backend_crud.backend_crud.dto.RelatedTaskDTO;
import backend_crud.backend_crud.dto.TaskAssociateDto;
import backend_crud.backend_crud.dto.TaskDTO;
import backend_crud.backend_crud.dto.TaskReqDTO;
import jakarta.validation.Valid;

@RequestMapping(path = "/task/manage")
public interface TaskRest {
    
    @PostMapping(path = "/add")
    public ResponseEntity<String> addTask(@RequestBody TaskReqDTO taskDto) throws UserNotFoundException;

    @PostMapping(path = "/mapuser")
    public ResponseEntity<String> mapUser(@RequestBody() TaskAssociateDto taskAssociateDto); 

    @GetMapping(path = "/admin/getTask")
    public ResponseEntity<List<TaskDTO>> getAllTask();

    @GetMapping(path = "/getAssociateTask")
    public ResponseEntity<List<TaskDTO>> getAssociateTask();

    @DeleteMapping(path = "/delete/associate")
    public ResponseEntity<String> deleteAssociate(@RequestParam("taskId") Integer taskId, @RequestParam("userId") Integer userId);
    
    @PatchMapping(path = "/addSubTask/{parentTaskId}/{subTaskId}")
    public ResponseEntity<String> asignChild(@PathVariable("parentTaskId") Integer parentTaskId, @PathVariable("subTaskId") Integer subTaskId);
    // TODO

    @PostMapping(path = "/addSubTask/{parentTaskId}")
    public ResponseEntity<String> addSubTask(@PathVariable("parentTaskId") Integer parentTaskId, @RequestBody() TaskDTO taskDto);
    //TODO

    @PostMapping(path = "/mergeSubtask")
    public ResponseEntity<String> setSubTask(@Valid @RequestBody() RelatedTaskDTO relatedTaskDTO); //TODO

    @GetMapping(path = "/getSubTask/{parentTaskId}")
    public ResponseEntity<Set<TaskDTO>> getSubTask(@PathVariable("parentTaskId") Integer taskId);
}
