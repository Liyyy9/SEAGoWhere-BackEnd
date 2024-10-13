package com.example.seagowhere.Controller;

import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Category;
import com.example.seagowhere.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody Category category) {
        return new ResponseEntity<>(categoryService.save(category), HttpStatus.CREATED);
    }

    // Read
        // Get a category by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id).orElseThrow(()-> new ResourceNotFoundException());

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

        // Get all categories
    @GetMapping
    public ResponseEntity<Object> getAllCategory() {
        List<Category> categoryList = categoryService.findAllCategory();
        if (categoryList.isEmpty()) throw new ResourceNotFoundException();

        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    // Update a category
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long id, @RequestBody @Valid Category categoryDetails) {
        Category checkCategory = categoryService.findById(id).map(_category -> {
            _category.setName(categoryDetails.getName());

            return categoryService.save(_category);
        }).orElseThrow(()-> new ResourceNotFoundException());

        return new ResponseEntity<>(checkCategory, HttpStatus.OK);
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        Category checkCategory = categoryService.findById(id).map(_category -> {
            categoryService.deleteById(_category.getId());
            return  _category;
        }).orElseThrow(()-> new ResourceNotFoundException());

        String response = String.format("%s has been successfully deleted.", checkCategory.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
