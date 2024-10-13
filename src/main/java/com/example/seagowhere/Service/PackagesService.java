package com.example.seagowhere.Service;

import com.example.seagowhere.Model.Category;
import com.example.seagowhere.Model.Packages;
import com.example.seagowhere.Repository.CategoryRepository;
import com.example.seagowhere.Repository.PackagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PackagesService {

    @Autowired
    private final PackagesRepository packagesRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    public PackagesService(PackagesRepository packagesRepository, CategoryRepository categoryRepository) {
        this.packagesRepository = packagesRepository;
        this.categoryRepository = categoryRepository;
    }
    // Create / Update a package
    public Packages save(Packages packages) {
        return packagesRepository.save(packages);
    }

    // Find a product by id
    public Optional<Packages> findById(Long id) {
        return packagesRepository.findById(id);
    }

    // Retrieve all packages
    public List<Packages> findAllPackages() {
        return packagesRepository.findAll();
    }

    //Find with PAGINATION
    public Map<String, Object> findWithPagination(int page, int perPage) {
        // Pageable object for pagination (0-based page index)
        PageRequest pageable = PageRequest.of(page - 1, perPage);
        Page<Packages> productPage = packagesRepository.findAll(pageable);

        // Prepare response map
        Map<String, Object> response = new HashMap<>();
        response.put("data", productPage.getContent());                   // List of Products
        response.put("page", productPage.getNumber() + 1);                // Current page (1-based)
        response.put("per_page", productPage.getSize());                  // Items per page
        response.put("total", productPage.getTotalElements());            // Total number of Products
        response.put("total_pages", productPage.getTotalPages());         // Total pages

        return response;
    }

    // Find package by category id
    public List<Packages> getPackagesByCategoryId(Long id){
        return packagesRepository.findByCategoryId(id);
    }

    // Delete package
    public void deleteById(Long id) {
        packagesRepository.deleteById(id);
    }

    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
}
