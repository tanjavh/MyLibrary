package com.example.onlineLibrary.userLoanMicroservice.service;

import com.example.onlineLibrary.libraryMicroservice.model.dto.BookInfoResponse;
import com.example.onlineLibrary.userLoanMicroservice.model.dto.LoanDto;
import com.example.onlineLibrary.userLoanMicroservice.model.entity.Loan;
import com.example.onlineLibrary.userLoanMicroservice.model.entity.User;
import com.example.onlineLibrary.userLoanMicroservice.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final RestTemplate restTemplate;

    private final String libraryBaseUrl = "http://localhost:8081/api/books";

    @Transactional
    public Loan createLoan(User user, Long bookId) {
        // Provera dostupnosti knjige preko REST API-ja
        BookInfoResponse book = restTemplate.getForObject(libraryBaseUrl + "/" + bookId, BookInfoResponse.class);
        if (book == null) {
            throw new RuntimeException("Knjiga ne postoji!");
        }
        if (!book.isAvailable()) {
            throw new RuntimeException("Knjiga nije dostupna!");
        }

        Loan loan = Loan.builder()
                .user(user)
                .bookId(bookId)
                .loanDate(LocalDate.now())
                .returned(false)
                .build();

        // Obeležavanje knjige kao nedostupne
        book.setAvailable(false);
        restTemplate.put(libraryBaseUrl + "/" + bookId, book);

        return loanRepository.save(loan);
    }

    @Transactional
    public void returnBook(Loan loan) {
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        // REST: označi knjigu kao dostupnu
        BookInfoResponse book = restTemplate.getForObject(libraryBaseUrl + "/" + loan.getBookId(), BookInfoResponse.class);
        if (book != null) {
            book.setAvailable(true);
            restTemplate.put(libraryBaseUrl + "/" + loan.getBookId(), book);
        }
    }

    public List<LoanDto> getLoansByUser(User user) {
        List<Loan> loans = loanRepository.findByUser(user);

        return loans.stream().map(loan -> {
            LoanDto dto = new LoanDto();
            dto.setLoanId(loan.getId());
            dto.setBookId(loan.getBookId());
            dto.setLoanDate(loan.getLoanDate());
            dto.setReturnDate(loan.getReturnDate());
            dto.setReturned(loan.isReturned());

            // REST poziv ka Library Service
            try {
                BookInfoResponse bookInfo = restTemplate.getForObject(
                        libraryBaseUrl + "/" + loan.getBookId(),
                        BookInfoResponse.class
                );
                if (bookInfo != null) {
                    dto.setBookTitle(bookInfo.getTitle());
                    dto.setBookAuthor(bookInfo.getAuthorName()); // ispravno ime polja
                    dto.setBookCategory(bookInfo.getCategory().name()); // enum u string
                }
            } catch (Exception e) {
                dto.setBookTitle("Unknown");
                dto.setBookAuthor("Unknown");
                dto.setBookCategory("Unknown");
            }

            return dto;
        }).toList();
    }
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    // ==============================
    // Dohvatanje svih pozajmica
    // ==============================
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}
