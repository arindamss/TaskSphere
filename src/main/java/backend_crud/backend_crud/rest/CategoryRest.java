package backend_crud.backend_crud.rest;

import backend_crud.backend_crud.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRest {

    @PostMapping(path = "/add")
    public ResponseEntity<String> add(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Category>> getAll();

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Integer id);

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Integer id);

    @PostMapping(path = "/update")
    public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap);
}
