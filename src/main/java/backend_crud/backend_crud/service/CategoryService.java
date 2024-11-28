package backend_crud.backend_crud.service;

import backend_crud.backend_crud.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


public interface CategoryService {
    public ResponseEntity<String> add(Map<String, String> requestMap);

    public ResponseEntity<List<Category>> getAll();

    public ResponseEntity<Category> getCategory(Integer id);

    public ResponseEntity<String> deleteCategory(Integer id);

    public ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
