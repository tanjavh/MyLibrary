package com.example.onlineLibrary.libraryMicroservice.model.dto;

import com.example.onlineLibrary.libraryMicroservice.model.enums.CategoryName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCreateDto {
    private String title;
    private Long authorId;           // ID izabrane opcije iz select
    private String newAuthorName;    // Ako korisnik želi novog autora
    private CategoryName category;   // Izbor iz enum
    private Integer year;
}
