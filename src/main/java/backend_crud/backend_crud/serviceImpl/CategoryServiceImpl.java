package backend_crud.backend_crud.serviceImpl;

import backend_crud.backend_crud.constents.CrudConstents;
import backend_crud.backend_crud.entity.Category;
import backend_crud.backend_crud.jwt.JWTAuthenticationFilter;
import backend_crud.backend_crud.repository.CategoryRepository;
import backend_crud.backend_crud.service.CategoryService;
import backend_crud.backend_crud.utills.CrudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private JWTAuthenticationFilter filter;

    @Override
    public ResponseEntity<String> add(Map<String, String> requestMap) {
        try{
            if(validateData(requestMap, false)){
                Category category=makeCategory(requestMap);
                categoryRepo.save(category);
                return new ResponseEntity<>("{\"message\":\"Category Save Successfully\"}", HttpStatus.OK);
            }
            return CrudUtils.getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    public boolean validateData(Map<String, String> requestMap, boolean valid){
        if(requestMap.containsKey("name") && requestMap.containsKey("desc")){
            if(requestMap.containsKey("id") && valid){
                return true;
            }
            else if(!valid){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public Category makeCategory(Map<String, String> requestMap){
        Category cat=new Category();
        cat.setName(requestMap.get("name"));
        cat.setDesc(requestMap.get("desc"));
        cat.setCreatedBy(filter.getCurrentUser());
        return cat;
    }

    @Override
    public ResponseEntity<List<Category>> getAll() {
        try{
            String username=filter.getCurrentUser();
            List<Category> categorys=categoryRepo.getAllCategory(username);
            return new ResponseEntity<>(categorys, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Category> getCategory(Integer id) {
        try{
            String username= filter.getCurrentUser();
            Optional<Category> cat=categoryRepo.getCategory(username, id);
            if(!cat.isEmpty()){
                return new ResponseEntity<>(cat.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Category(), HttpStatus.BAD_REQUEST);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Category(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {
        try{
            Optional<Category> cat=categoryRepo.findById(id);
            if(!cat.isEmpty()){
               Category c=cat.get();
               if(c.getCreatedBy().equalsIgnoreCase(filter.getCurrentUser())){
                   categoryRepo.delete(c);
                   return CrudUtils.getResponseEntity("Category Deleted Successfully", HttpStatus.OK);
               }
               return CrudUtils.getResponseEntity("{UnAuthorized To Access}", HttpStatus.UNAUTHORIZED);
            }
            return CrudUtils.getResponseEntity("Id Does Not Exist", HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(filter.isUser()){
                if(validateData(requestMap, true)){
                    Optional<Category> cat=categoryRepo.findById(Integer.parseInt(requestMap.get("id")));
//                    System.out.println("Category : "+cat.get());
                    if(!cat.isEmpty()){
                        if(cat.get().getCreatedBy().equals(filter.getCurrentUser())) {
                            Category updatedCategory = getCategory(requestMap);
                            categoryRepo.save(updatedCategory);
                            return new ResponseEntity<>("{\"message\":\"Category Updated Successfully\"}", HttpStatus.OK);
                        }
                        return CrudUtils.getResponseEntity("Unauthorized for this action", HttpStatus.UNAUTHORIZED);
                    }
                    return CrudUtils.getResponseEntity("Id Does Not Exist", HttpStatus.BAD_REQUEST);
                }
                return CrudUtils.getResponseEntity(CrudConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CrudUtils.getResponseEntity("User not found.", HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CrudUtils.getResponseEntity(CrudConstents.SOMETHING_WNNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Category getCategory(Map<String, String> requestMap){
        Category category=new Category();
        category.setId(Integer.parseInt(requestMap.get("id")));
        category.setName(requestMap.get("name"));
        category.setDesc(requestMap.get("desc"));
        category.setCreatedBy(filter.getCurrentUser());
        return category;
    }
}































