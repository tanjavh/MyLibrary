package com.example.onlineLibrary.repository;

import com.example.onlineLibrary.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    // Opcionalno: pronalazi knjigu po naslovu
    Optional<Book> findByTitle(String title);

    // Opcionalno: proverava da li knjiga sa naslovom postoji
    boolean existsByTitle(String title);
}
