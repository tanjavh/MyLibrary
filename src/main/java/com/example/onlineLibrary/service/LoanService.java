package com.example.onlineLibrary.service;

import com.example.onlineLibrary.model.entity.Book;
import com.example.onlineLibrary.model.entity.Loan;
import com.example.onlineLibrary.model.entity.User;
import com.example.onlineLibrary.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    // Kreiranje nove pozajmice
    @Transactional
    public Loan createLoan(User user, Book book) {
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(LocalDate.now())
                .returned(false)
                .build();
        return loanRepository.save(loan);
    }

    // Vraćanje knjige
    @Transactional
    public void returnBook(Loan loan) {
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);
    }

    // Sve pozajmice korisnika
    public List<Loan> getLoansByUser(User user) {
        return loanRepository.findByUser(user);
    }

    // Sve pozajmice
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // Dohvatanje po ID

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }
}

