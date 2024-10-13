package com.example.seagowhere.Service;

import com.example.seagowhere.Model.Category;
import com.example.seagowhere.Model.Packages;
import com.example.seagowhere.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Create / Update category
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // Find category by id
    public Optional<Category> findById(Long id){
        return categoryRepository.findById(id);
    }

    // Retrieve all categories
    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    // Delete category
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

}
