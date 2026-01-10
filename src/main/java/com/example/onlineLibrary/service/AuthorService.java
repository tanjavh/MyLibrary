package com.example.onlineLibrary.service;

import com.example.onlineLibrary.model.entity.Author;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.example.onlineLibrary.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    // ==============================
    // Dohvata sve autore
    // ==============================
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    // ==============================
    // Čuvanje ili kreiranje novog autora
    // ==============================
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    // ==============================
    // Dohvata autora po ID-ju
    // ==============================
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    // ==============================
    // Brisanje autora
    // ==============================
    public void delete(Author author) {
        if (author != null) {
            authorRepository.delete(author);
        }
    }
}

