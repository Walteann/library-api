package com.walteann.libraryapi.model.repository;

import java.time.LocalDate;
import java.util.List;

import com.walteann.libraryapi.model.entity.Book;
import com.walteann.libraryapi.model.entity.Loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "  SELECT CASE WHEN ( count(l.id) > 0 )  THEN true ELSE false END " + 
    " FROM Loan l WHERE l.book =:book AND (l.returned is null or l.returned is true)  ")
    boolean existsByBookAndNotReturned(@Param("book") Book book);

    @Query(value = " SELECT L FROM Loan as L join L.book as B where B.isbn = :isbn or L.customer = :customer")
    Page<Loan> findByBookIsbnOrCustomer(
            @Param("isbn") String isbn, 
            @Param("customer") String customer, 
            Pageable pageable);

    Page<Loan> findByBook(Book book, Pageable pageable);

    @Query(value = " SELECT L from Loan L WHERE L.loanDate <= :threeDaysAgo AND (L.returned is null or L.returned is true)")
    List<Loan> findByLoanDateLessThanAndNotReturned(@Param("threeDaysAgo") LocalDate threeDaysAgo);
    
}
