package com.example.seagowhere.Repository;

import com.example.seagowhere.Model.Category;
import com.example.seagowhere.Model.Packages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackagesRepository extends JpaRepository<Packages, Long> {
    // CRUD operations inherited from JpaRepository

    List<Packages> findByCategoryId(Long categoryId);
}
