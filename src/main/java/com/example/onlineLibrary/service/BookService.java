package com.example.onlineLibrary.service;

import com.example.onlineLibrary.model.entity.Book;
import com.example.onlineLibrary.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Dohvata sve knjige
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // Dohvata knjigu po ID-ju
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    // Čuvanje knjige
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    // Brisanje knjige
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
