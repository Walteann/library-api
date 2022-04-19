package com.walteann.libraryapi.model.repository;

import com.walteann.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
    
    Book getById(Long id);

    // void delete(Book book);

    // Book update(Book book);
}
