package com.example.seagowhere.Repository;

import com.example.seagowhere.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // CRUD operations inherited from JpaRepository
}
