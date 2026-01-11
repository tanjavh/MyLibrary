package com.example.onlineLibrary.libraryMicroservice.repository;

import com.example.onlineLibrary.libraryMicroservice.model.entity.Category;
import com.example.onlineLibrary.libraryMicroservice.model.enums.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(CategoryName name);
}
