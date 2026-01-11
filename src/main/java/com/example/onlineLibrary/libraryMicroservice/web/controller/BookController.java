package com.example.onlineLibrary.libraryMicroservice.web.controller;

import com.example.onlineLibrary.libraryMicroservice.model.dto.BookCreateDto;
import com.example.onlineLibrary.libraryMicroservice.model.entity.Author;
import com.example.onlineLibrary.libraryMicroservice.model.entity.Book;
import com.example.onlineLibrary.libraryMicroservice.model.entity.Category;
import com.example.onlineLibrary.libraryMicroservice.model.enums.CategoryName;
import com.example.onlineLibrary.libraryMicroservice.service.AuthorService;
import com.example.onlineLibrary.libraryMicroservice.service.BookService;
import com.example.onlineLibrary.libraryMicroservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    // ==============================
    // LISTA SVIH KNJIGA
    // ==============================
    @GetMapping
    public String allBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books"; // ime template-a
    }

    // ==============================
    // FORMA ZA KREIRANJE KNJIGE
    // ==============================
    @GetMapping("/create")
    public String createBookForm(Model model) {
        model.addAttribute("book", new BookCreateDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("categories", Arrays.asList(CategoryName.values()));
        return "create-book";
    }

    // ==============================
    // POST: KREIRANJE KNJIGE
    // ==============================
    @PostMapping("/create")
    public String createBook(@ModelAttribute("book") BookCreateDto dto, Model model) {

        Author author = null;

        // 1️⃣ Autor: novi ili izabrani
        if (dto.getNewAuthorName() != null && !dto.getNewAuthorName().isBlank()) {
            author = new Author();
            author.setName(dto.getNewAuthorName());
            author = authorService.save(author);
        } else if (dto.getAuthorId() != null) {
            author = authorService.findById(dto.getAuthorId())
                    .orElse(null);
            if (author == null) {
                model.addAttribute("error", "Izabrani autor ne postoji!");
                model.addAttribute("authors", authorService.findAll());
                model.addAttribute("categories", Arrays.asList(CategoryName.values()));
                return "create-book";
            }
        } else {
            model.addAttribute("error", "Morate uneti autora!");
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("categories", Arrays.asList(CategoryName.values()));
            return "create-book";
        }

        // 2️⃣ Kategorija
        if (dto.getCategory() == null) {
            model.addAttribute("error", "Morate izabrati kategoriju!");
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("categories", Arrays.asList(CategoryName.values()));
            return "create-book";
        }
        Category category = categoryService.findByName(dto.getCategory())
                .orElseGet(() -> categoryService.save(new Category(dto.getCategory())));

        // 3️⃣ Kreiranje knjige
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(author)
                .category(category)
                .year(dto.getYear() != null ? dto.getYear() : 2000)
                .available(true)
                .build();

        bookService.save(book);

        return "redirect:/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
