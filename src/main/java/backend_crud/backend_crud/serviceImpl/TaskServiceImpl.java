package backend_crud.backend_crud.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend_crud.backend_crud.ExceptionHandler.TaskNotFoundException;
import backend_crud.backend_crud.ExceptionHandler.UserNotFoundException;
import backend_crud.backend_crud.constents.CrudConstents;
import backend_crud.backend_crud.dto.RelatedTaskDTO;
import backend_crud.backend_crud.dto.TaskAssociateDto;
import backend_crud.backend_crud.dto.TaskDTO;
import backend_crud.backend_crud.dto.TaskReqDTO;
import backend_crud.backend_crud.entity.Category;
import backend_crud.backend_crud.entity.Task;
import backend_crud.backend_crud.entity.User;
import backend_crud.backend_crud.jwt.JWTAuthenticationFilter;
import backend_crud.backend_crud.repository.CategoryRepository;
import backend_crud.backend_crud.repository.TaskRepository;
import backend_crud.backend_crud.repository.UserRepository;
import backend_crud.backend_crud.service.TaskService;
import backend_crud.backend_crud.utills.CrudUtils;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private JWTAuthenticationFilter filter;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ResponseEntity<String> addTask(TaskReqDTO taskReqDto) throws UserNotFoundException {

        try {

            Optional<Category> cate = categoryRepo.findById(taskReqDto.getCategoryId());
            Category category = null;
            if (cate.isPresent()) {
                category = cate.get();
            } else {
                return CrudUtils.getResponseEntity("Category Not Found", HttpStatus.BAD_REQUEST);
            }

            Set<User> users = taskReqDto.getAssociateIds().stream()
                    .map(u -> userRepo.findById(u)
                            .orElseThrow(() -> new RuntimeException("User Not Found: " + u)))
                    .collect(Collectors.toSet());

            Task parentTask = null;

            Optional<Task> superTask = taskRepo
                    .findById(taskReqDto.getParentTaskId() == null ? 0 : taskReqDto.getParentTaskId());

            if (superTask.isPresent()) {
                parentTask = superTask.get();
            }

            Task task = new Task();
            task.setName(taskReqDto.getName());
            task.setDuration(taskReqDto.getDuration());
            task.setAssociate(users);
            task.setCategory(category);
            task.setParentTask(parentTask);
            task.setCreatedBy(filter.getCurrentUser());

            taskRepo.save(task);

            for (User u : users) {
                u.getTasks().add(task);
                userRepo.save(u);
            }

            return CrudUtils.getResponseEntity("TaskAdded Succesfully", HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> mapUser(TaskAssociateDto associateDto) {

        System.out.println("I am called : ");

        try {
            if (filter.isAdmin()) {
                if (associateDto.getTaskId() != null) {
                    Optional<Task> task = taskRepo.findById(associateDto.getTaskId());

                    System.out.println("Task is : " + task.get().getName());

                    List<Integer> uasrsId = associateDto.getUsersIds();
                    Set<User> users = uasrsId.stream()
                            .map(u -> userRepo.findById(u)
                                    .orElseThrow(() -> new RuntimeException("User Not Found")))
                            .collect(Collectors.toSet());

                    Task t = task.get();
                    t.getAssociate().addAll(users);
                    taskRepo.saveAndFlush(t);

                    return CrudUtils.getResponseEntity("Users Associations Successfull", HttpStatus.CREATED);
                } else {
                    System.out.println("Task is null");
                }

            }
            return CrudUtils.getResponseEntity("UnAuthorized Access", HttpStatus.UNAUTHORIZED);

        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return CrudUtils.getResponseEntity("No Such Task Is Present", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return CrudUtils.getResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getAllTask() {
        try {
            String currentUser = filter.getCurrentUser();
            List<Task> tasks = this.taskRepo.findByCreatedBy(currentUser);

            List<TaskDTO> taskDtos = new ArrayList<>();
            for (Task task : tasks) {
                TaskDTO taskDto = new TaskDTO();
                taskDto.setId(task.getId());
                taskDto.setName(task.getName());
                taskDto.setDuration(task.getDuration());
                taskDto.setCreatedBy(task.getCreatedBy());
                taskDto.setParentTask(task.getParentTask() != null ? task.getParentTask().getId() : null);
                taskDto.getAssociateIds()
                        .addAll(task.getAssociate().stream().map(u -> u.getId()).collect(Collectors.toList()));
                taskDto.setCategoryId(task.getCategory().getId());

                taskDtos.add(taskDto);
            }
            return new ResponseEntity<>(taskDtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteAssociate(Integer taskId, Integer userId) {
        try {
            if (filter.isAdmin()) {
                Optional<Task> t = taskRepo.findById(taskId);
                if (!Objects.isNull(t.get())) {
                    Task task = t.get();

                    Optional<User> u = userRepo.findById(userId);
                    if (!Objects.isNull(u)) {
                        User user = u.get();

                        task.setAssociate(task.getAssociate().stream()
                                .filter(id -> id != user)
                                .collect(Collectors.toSet()));

                        taskRepo.saveAndFlush(task);
                        return CrudUtils.getResponseEntity("All oka", HttpStatus.OK);
                    }
                    return CrudUtils.getResponseEntity("User is not present", HttpStatus.BAD_REQUEST);

                    // System.out.println("Task : "+task.getName());

                }
                return CrudUtils.getResponseEntity("Task is not present", HttpStatus.BAD_REQUEST);

            }
            return CrudUtils.getResponseEntity("UnAuthorized Access", HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Id Is Not present", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> setSubTask(Integer parentTaskId, Integer subTaskId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSubTask'");
    }

    @Override
    public ResponseEntity<String> addSubTask(Integer parentTaskId, TaskDTO taskDto) {
    
        throw new UnsupportedOperationException("Unimplemented method 'addSubTask'");
    }


    /* Fetch all the task to which the user is associated */
    @Override
    public ResponseEntity<List<TaskDTO>> getAssociateTask() {
        try {
            User user = userRepo.findByEmailId(filter.getCurrentUser());

            List<Task> taskes = taskRepo.findAssociateTasks(user.getId());

            List<TaskDTO> taskDtoRes = new ArrayList();

            for(Task task : taskes){
                System.out.println("Task : "+task.getName());
                // System.out.println(task.getAssociate());
                TaskDTO taskDto = new TaskDTO();
                taskDto.setId(task.getId());
                taskDto.setName(task.getName());
                taskDto.setDuration(task.getDuration());
                taskDto.setCreatedBy(task.getCreatedBy());
                taskDto.setCategoryId(task.getCategory().getId());
                taskDto.getRelatedTaskes()
                        .addAll(task.getRelatedTaskes().stream().map(t -> t.getId()).collect(Collectors.toList()));
                taskDto.setParentTask(task.getParentTask() == null ? null : task.getParentTask().getId());
                taskDto.getAssociateIds().addAll(task.getAssociate().stream().map(u -> u.getId()).collect(Collectors.toList()));

                taskDtoRes.add(taskDto);
            }

            return new ResponseEntity<>(taskDtoRes, HttpStatus.OK); 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> setSubTask(RelatedTaskDTO relatedTaskDTO){

        try{

            Optional<Task> parentTask = taskRepo.findById(relatedTaskDTO.getParentTaskId());
            if(parentTask.isPresent()){
                Task task = parentTask.get();
            
                Set<Task> relatedTasks = relatedTaskDTO.getRelatedTaskIds().stream()
                                            .map(t ->{
                                                Optional<Task> optionalTask = taskRepo.findById(t);
                                                if(optionalTask.isPresent()){
                                                    Task childTask = optionalTask.get();
                                                    childTask.setParentTask(task);
                                                    taskRepo.saveAndFlush(childTask);
                                                    return childTask;
                                                }
                                                else{
                                                    throw new RuntimeException(new TaskNotFoundException("Task is not found with id : "+t));
                                                }
                                            }).collect(Collectors.toSet());

                System.out.println("Size of Related Task :"+task.getRelatedTaskes().size());

                task.getRelatedTaskes().addAll(relatedTasks);

                System.out.println("Size of Related Task After :"+task.getRelatedTaskes().size());

                try{
                    taskRepo.save(task);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("I am done");

                return CrudUtils.getResponseEntity("Tasks merge successfull", HttpStatus.CREATED);

            }
            return CrudUtils.getResponseEntity("Error whiling parent task fetching", HttpStatus.BAD_REQUEST);

        }
        catch(RuntimeException e){
            if(e.getCause() instanceof TaskNotFoundException){
                return CrudUtils.getResponseEntity(e.getCause().getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            System.out.println("Yes, I am called");
            e.printStackTrace();
            return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Missing", HttpStatus.NOT_FOUND);
        
        
    }

    @Override
    public ResponseEntity<Set<TaskDTO>> getSubTask(Integer taskId){
        try{
            Optional<Task> optionalTask = taskRepo.findTaskByUserAndId(filter.getCurrentUser(), taskId);
            if(optionalTask.isPresent()){
                Task task = optionalTask.get();
                Set<Task> subTasks = task.getRelatedTaskes();
                Set<TaskDTO> taskRes = subTasks.stream()
                                            .map(t-> {
                                                TaskDTO taskDto=new TaskDTO();
                                                taskDto.setId(t.getId());
                                                taskDto.setName(t.getName());
                                                taskDto.setDuration(t.getDuration());
                                                taskDto.setCreatedBy(t.getCreatedBy());
                                                taskDto.setParentTask(t.getParentTask().getId());
                                                taskDto.setCategoryId(t.getCategory().getId());
                                                taskDto.getRelatedTaskes().addAll(task.getRelatedTaskes().stream().map(tsk -> tsk.getId()).collect(Collectors.toSet()));
                                                taskDto.getAssociateIds().addAll(task.getAssociate().stream().map(tsk -> tsk.getId()).collect(Collectors.toSet()));
                                                return taskDto;
                                            }).collect(Collectors.toSet());

                return new ResponseEntity<>(taskRes, HttpStatus.OK);
            }
            return new ResponseEntity<>(new HashSet<>(), HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new HashSet<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
