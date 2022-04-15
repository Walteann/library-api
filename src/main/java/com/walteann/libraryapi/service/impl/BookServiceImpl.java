package com.walteann.libraryapi.service.impl;

import com.walteann.libraryapi.exception.BussinessException;
import com.walteann.libraryapi.model.entity.Book;
import com.walteann.libraryapi.model.repository.BookRepository;
import com.walteann.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BussinessException("Isbn já cadastrado.");
        }
        return repository.save(book);
    }
}
