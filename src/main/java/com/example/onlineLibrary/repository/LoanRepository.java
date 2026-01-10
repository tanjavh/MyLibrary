package com.example.onlineLibrary.repository;

import com.example.onlineLibrary.model.entity.Book;
import com.example.onlineLibrary.model.entity.Loan;
import com.example.onlineLibrary.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository  extends JpaRepository<Loan, Long> {
    // Sve pozajmice određenog korisnika
    List<Loan> findByUser(User user);

    // Sve aktivne pozajmice određenog korisnika
    List<Loan> findByUserAndReturnedFalse(User user);

    // Sve aktivne pozajmice određene knjige
    List<Loan> findByBookAndReturnedFalse(Book book);
}
