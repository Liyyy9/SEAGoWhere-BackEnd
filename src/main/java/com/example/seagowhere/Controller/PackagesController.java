package com.example.seagowhere.Controller;

import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Category;
import com.example.seagowhere.Model.Packages;
import com.example.seagowhere.Service.PackagesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PackagesController {

    @Autowired
    private final PackagesService packagesService;

    public PackagesController(PackagesService packagesService) {
        this.packagesService = packagesService;
    }

    // Create a new package
    @RequestMapping("/admin/api/packages")
    @PostMapping
    public ResponseEntity<Object> createPackage(@Valid @RequestBody Packages packages){
        return new ResponseEntity<>(packagesService.save(packages), HttpStatus.CREATED);
    }

    // Get a package by id
    //@RequestMapping("/public/api/packages")
    @GetMapping("/public/api/packages/{id}")
    public ResponseEntity<Object> getPackageById(@PathVariable Long id) {
        Packages packages = packagesService.findById(id).orElseThrow(()-> new ResourceNotFoundException());
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    // Retrieve all packages
    @RequestMapping("/public/api/packages")
    @GetMapping
    public ResponseEntity<Object> getAllPackages() {
        List<Packages> packagesList = packagesService.findAllPackages();
        if (packagesList.isEmpty()) throw new ResourceNotFoundException();

        return new ResponseEntity<>(packagesList, HttpStatus.OK);
    }

    // Get package with PAGINATION
    //@RequestMapping("/public/api/packages")
    @GetMapping("/public/api/packages/listing")
    public ResponseEntity<Object> getPackagesWithPagination (
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int perPage
    ) {
        return new ResponseEntity<>(packagesService.findWithPagination(page, perPage), HttpStatus.OK);
    }

    // Get a package by category id
    //@RequestMapping("/public/api/packages")
    @GetMapping("/public/api/packages/category/{categoryId}")
    public ResponseEntity<Object> getPackagesByCategoryId (@PathVariable Long categoryId){
        List<Packages> packages = packagesService.getPackagesByCategoryId(categoryId);
        if (packages.isEmpty()) throw new ResourceNotFoundException();

        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    // Update a package
    //@RequestMapping("/admin/api/packages")
    @PutMapping("/admin/api/packages/{id}")
    public ResponseEntity<Object> updatePackage(@PathVariable Long id, @Valid @RequestBody Packages packageDetails) {

        Packages checkPackage = packagesService.findById(id).map(_package -> {
            Category category = packagesService.findCategoryById(packageDetails.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + packageDetails.getCategory().getId()));

          _package.setCategory(category);
          _package.setName(packageDetails.getName());
          _package.setCountry(packageDetails.getCountry());
          _package.setBlurb(packageDetails.getBlurb());
          _package.setDesc(packageDetails.getDesc());
          _package.setPrice(packageDetails.getPrice());
          _package.setStart_date(packageDetails.getStart_date());
          _package.setEnd_date(packageDetails.getEnd_date());
          _package.setNo_of_days(packageDetails.getNo_of_days());
          _package.setNo_of_nights(packageDetails.getNo_of_nights());
          _package.setImage_url(packageDetails.getImage_url());
          _package.setImage_1(packageDetails.getImage_1());
          _package.setImage_2(packageDetails.getImage_2());
          _package.setImage_3(packageDetails.getImage_3());
          _package.setImage_4(packageDetails.getImage_4());
          return packagesService.save(_package);
        }).orElseThrow(()-> new ResourceNotFoundException());

        return new ResponseEntity<>(checkPackage, HttpStatus.OK);
    }

    // Delete a package
    //@RequestMapping("/admin/api/packages")
    @DeleteMapping("/admin/api/packages/{id}")
    public ResponseEntity<Object> deletePackage(@PathVariable Long id) {
        Packages checkPackages = packagesService.findById(id).map(_packages -> {
            packagesService.deleteById(_packages.getId());
            return _packages;
        }).orElseThrow(()-> new ResourceNotFoundException());

        String response = String.format("%s has been successfully deleted.", checkPackages.getName());

        return new ResponseEntity<>(checkPackages, HttpStatus.OK);
    }
}
