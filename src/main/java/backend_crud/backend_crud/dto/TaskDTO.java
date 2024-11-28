package backend_crud.backend_crud.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TaskDTO {
    private Integer id;
    private String name;
    private int duration;
    private String createdBy;
    private List<Integer> relatedTaskes = new ArrayList<>();
    private Integer parentTask;
    private List<Integer> associateIds = new ArrayList<>();
    private Integer categoryId;
}
