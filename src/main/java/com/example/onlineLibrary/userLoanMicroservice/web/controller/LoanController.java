package com.example.onlineLibrary.userLoanMicroservice.web.controller;

import com.example.onlineLibrary.userLoanMicroservice.model.dto.LoanDto;
import com.example.onlineLibrary.userLoanMicroservice.model.entity.Loan;
import com.example.onlineLibrary.userLoanMicroservice.model.entity.User;
import com.example.onlineLibrary.userLoanMicroservice.service.LoanService;
import com.example.onlineLibrary.userLoanMicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final UserService userService;

    // ==============================
    // Pozajmljivanje knjige
    // ==============================
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long userId,
                             @RequestParam Long bookId,
                             RedirectAttributes redirectAttributes) {

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nepostojeći korisnik"));

        try {
            loanService.createLoan(user, bookId);
            redirectAttributes.addFlashAttribute("success", "Knjiga uspešno pozajmljena!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // Vraćamo na korisnički pregled pozajmica
        return "redirect:/loans/my";
    }

    // ==============================
    // Vraćanje knjige
    // ==============================
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Loan> optionalLoan = loanService.getLoanById(id);

        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();

            if (!loan.isReturned()) {
                loanService.returnBook(loan);
                redirectAttributes.addFlashAttribute("success", "Knjiga je uspešno vraćena!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Knjiga je već vraćena.");
            }

            // Proveravamo da li je trenutni korisnik vlasnik pozajmice
            User currentUser = userService.getCurrentUser();
            if (loan.getUser().getId().equals(currentUser.getId())) {
                return "redirect:/loans/my";
            } else {
                return "redirect:/loans/all";
            }

        } else {
            redirectAttributes.addFlashAttribute("error", "Pozajmica nije pronađena!");
            return "redirect:/loans/all";
        }
    }

    // ==============================
    // Pregled pozajmica trenutnog korisnika
    // ==============================
    @GetMapping("/my")
    public String myLoans(Model model) {
        User currentUser = userService.getCurrentUser();
        List<LoanDto> loans = loanService.getLoansByUser(currentUser);
        model.addAttribute("loans", loans);
        return "user-loans";
    }

    // ==============================
    // Pregled svih pozajmica određenog korisnika (admin)
    // ==============================
    @GetMapping("/user/{userId}")
    public String viewUserLoans(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nepostojeći korisnik"));

        List<LoanDto> loans = loanService.getLoansByUser(user);
        model.addAttribute("loans", loans);
        model.addAttribute("user", user);
        return "user-loans";
    }

    // ==============================
    // Pregled svih pozajmica (admin)
    // ==============================
    @GetMapping("/all")
    public String viewAllLoans(Model model) {
        List<Loan> loans = loanService.getAllLoans();
        model.addAttribute("loans", loans);
        return "all-loans";
    }
}
