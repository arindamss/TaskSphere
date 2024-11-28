package backend_crud.backend_crud.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RelatedTaskDTO {
    
    @NotNull
    private Integer parentTaskId;
    
    @NotNull
    @Size(min = 1)
    private List<@Min(1) Integer> relatedTaskIds; 

}
