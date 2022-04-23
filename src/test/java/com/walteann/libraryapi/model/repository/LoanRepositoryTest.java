package com.walteann.libraryapi.model.repository;

import com.walteann.libraryapi.model.entity.Book;
import com.walteann.libraryapi.model.entity.Loan;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.walteann.libraryapi.model.repository.BookRepositoryTest.createNewBook;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("deve verificar se existe emprestimo não devolvido para o livro")
    void existsByBookAndNotReturnedTest() {

        // Cenario

        Loan loan = createAndPersistLoan(LocalDate.now());

        Book book = loan.getBook();

        // Execucao
        boolean exists = repository.existsByBookAndNotReturned(book);

        Assertions.assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve buscar emprestimo pelo isbn do livro ou customer")
    void findByBookIsbnOrCustomerTest() {

        Loan loan = createAndPersistLoan(LocalDate.now());

        Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));

        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent()).contains(loan);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve obter emprestimos cuja a data emprestimo for menor ou igual a 3 dias atras e nao retornados")
    void findByLoanDateLessThanAndNotReturnedTest() {
        Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

        List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
        
        Assertions.assertThat(result).hasSize(1).contains(loan);
    }

    @Test
    @DisplayName("Deve retornar vazio quando não houver emprestimos atrasados.")
    void notFindByLoanDateLessThanAndNotReturnedTest() {
        createAndPersistLoan(LocalDate.now());

        List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
        
        Assertions.assertThat(result).isEmpty();
    }

    public Loan createAndPersistLoan(LocalDate loanDate) {
        Book book = createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder()
                .book(book)
                .customer("Fulano")
                .loanDate(loanDate)
                .build();

        entityManager.persist(loan);

        return loan;
    }

}
