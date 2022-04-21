package com.walteann.libraryapi.model.repository;

import java.util.Optional;

import com.walteann.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
    
    Book getById(Long id);

    Optional<Book> findByIsbn(String isbn);

}
