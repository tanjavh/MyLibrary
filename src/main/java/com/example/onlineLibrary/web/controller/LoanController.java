package com.example.onlineLibrary.web.controller;

import com.example.onlineLibrary.model.entity.Book;
import com.example.onlineLibrary.model.entity.Loan;
import com.example.onlineLibrary.model.entity.User;
import com.example.onlineLibrary.service.BookService;
import com.example.onlineLibrary.service.LoanService;
import com.example.onlineLibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final UserService userService;
    private final BookService bookService;

    // ==============================
    // KORISNIK VIDI SVOJE POZAJMICE
    // ==============================
    @GetMapping("/my")
    public String myLoans(Model model) {
        User currentUser = userService.getCurrentUser();
        List<Loan> loans = loanService.getLoansByUser(currentUser);
        model.addAttribute("loans", loans);
        return "loans/my-loans"; // Thymeleaf template
    }

    // ==============================
    // ADMIN VIDI SVE POZAJMICE
    // ==============================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public String allLoans(Model model) {
        List<Loan> loans = loanService.getAllLoans(); // sve pozajmice
        model.addAttribute("loans", loans);
        return "loans/all-loans"; // Thymeleaf template
    }

    // ==============================
    // PRIKAZ FORME ZA KREIRANJE POZAJMNICE
    // ==============================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createLoanForm(Model model) {
        model.addAttribute("users", userService.findAll()); // svi korisnici
        model.addAttribute("books", bookService.findAll()); // sve knjige
        return "loans/create-loan"; // Thymeleaf template
    }

    // ==============================
    // ADMIN KREIRA NOVU POZAJMICU
    // ==============================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createLoan(@RequestParam Long userId,
                             @RequestParam Long bookId) {

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        loanService.createLoan(user, book);

        return "redirect:/loans/all";
    }

    // ==============================
    // ADMIN VRAĆA KNJIGU
    // ==============================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loanService.returnBook(loan);
        return "redirect:/loans/all";
    }
}
