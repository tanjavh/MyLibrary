package com.example.onlineLibrary.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ko je pozajmio knjigu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Koju knjigu je pozajmio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Datum kada je pozajmljena knjiga
    @Column(nullable = false)
    private LocalDate loanDate;

    // Datum kada je knjiga vraćena
    private LocalDate returnDate;

    // Da li je knjiga vraćena
    private boolean returned;
}
