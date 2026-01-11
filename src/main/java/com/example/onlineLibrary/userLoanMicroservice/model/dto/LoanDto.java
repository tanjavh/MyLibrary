package com.example.onlineLibrary.userLoanMicroservice.model.dto;



import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDto {
    private Long loanId;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookCategory;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;
}

