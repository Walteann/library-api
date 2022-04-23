package com.walteann.libraryapi.service;

import java.util.List;
import java.util.Optional;

import com.walteann.libraryapi.api.dto.LoanFilterDTO;
import com.walteann.libraryapi.model.entity.Book;
import com.walteann.libraryapi.model.entity.Loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO any, Pageable any2);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);

    List<Loan> getAllLateLoans();
}
