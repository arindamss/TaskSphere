package backend_crud.backend_crud.restImpl;

import backend_crud.backend_crud.constents.CrudConstents;
import backend_crud.backend_crud.entity.Category;
import backend_crud.backend_crud.rest.CategoryRest;
import backend_crud.backend_crud.service.CategoryService;
import backend_crud.backend_crud.utills.CrudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class CategortRestImpl implements CategoryRest {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseEntity<String> add(Map<String, String> requestMap) {
        try{
            return categoryService.add(requestMap);
        }
        catch(Exception e){
            return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Category>> getAll(){
        try{
            return categoryService.getAll();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Category> getCategory(Integer id) {
        try{
            return categoryService.getCategory(id);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Category(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {
        try{
            return categoryService.deleteCategory(id);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap){
        try{
            return categoryService.updateCategory(requestMap);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
