package backend_crud.backend_crud.restImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import backend_crud.backend_crud.ExceptionHandler.UserNotFoundException;
import backend_crud.backend_crud.constents.CrudConstents;
import backend_crud.backend_crud.dto.RelatedTaskDTO;
import backend_crud.backend_crud.dto.TaskAssociateDto;
import backend_crud.backend_crud.dto.TaskDTO;
import backend_crud.backend_crud.dto.TaskReqDTO;
import backend_crud.backend_crud.rest.TaskRest;
import backend_crud.backend_crud.service.TaskService;
import backend_crud.backend_crud.utills.CrudUtils;

@RestController
public class TaskRestImpl implements TaskRest {

    @Autowired
    private TaskService taskService;
    

    @Override
    public ResponseEntity<String> addTask(TaskReqDTO taskDto) throws UserNotFoundException {
        try {
            return taskService.addTask(taskDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> mapUser(TaskAssociateDto associateDto) {
        try{
            return taskService.mapUser(associateDto);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<List<TaskDTO>> getAllTask() {
        try{
            return taskService.getAllTask();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @Override
    public ResponseEntity<String> deleteAssociate(Integer taskId, Integer userId){
        try{
            return taskService.deleteAssociate( taskId, userId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> asignChild(Integer parentTaskId, Integer subTaskId) {
        try {
            return taskService.setSubTask(parentTaskId, subTaskId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> addSubTask(Integer parentTaskId, TaskDTO taskDto) {
        try {
            return taskService.addSubTask(parentTaskId, taskDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<List<TaskDTO>> getAssociateTask() {
       try {
           return taskService.getAssociateTask();
       } catch (Exception e) {
        System.out.println("I am called from Rest Impl");
            e.printStackTrace();
       }
       return new ResponseEntity(new ArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> setSubTask(RelatedTaskDTO relatedTaskDTO) {
        try {
            return taskService.setSubTask(relatedTaskDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Set<TaskDTO>> getSubTask(Integer taskId) {
        try{
            return taskService.getSubTask(taskId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashSet<>());
    }

    

    
}
