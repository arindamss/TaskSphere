package backend_crud.backend_crud.dto;

import java.util.List;

import lombok.Data;

@Data
public class TaskAssociateDto {
    Integer taskId;
    List<Integer> usersIds; 
}
