package com.example.onlineLibrary.service;

import com.example.onlineLibrary.model.entity.Category;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.example.onlineLibrary.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // ==============================
    // Dohvata sve kategorije
    // ==============================
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // ==============================
    // Čuvanje ili kreiranje nove kategorije
    // ==============================
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // ==============================
    // Dohvata kategoriju po ID-ju
    // ==============================
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    // ==============================
    // Brisanje kategorije
    // ==============================
    public void delete(Category category) {
        if (category != null) {
            categoryRepository.delete(category);
        }
    }
}

