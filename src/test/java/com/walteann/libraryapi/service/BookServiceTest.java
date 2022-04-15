package com.walteann.libraryapi.service;

import com.walteann.libraryapi.exception.BussinessException;
import com.walteann.libraryapi.model.entity.Book;
import com.walteann.libraryapi.model.repository.BookRepository;
import com.walteann.libraryapi.service.impl.BookServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        Book book = createValidBook();
        Mockito.when( repository.existsByIsbn(Mockito.anyString()) ).thenReturn(false);

        Mockito.when(repository.save(book)).thenReturn(Book.builder().id(1L).author("Uncle Bob").title("Clean Code").isbn("123").build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("Clean Code");
        assertThat(savedBook.getAuthor()).isEqualTo("Uncle Bob");
    }

    @Test
    @DisplayName("Deve lançar error de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN() {

        // Cenario
        Book book = createValidBook();
        Mockito.when( repository.existsByIsbn(Mockito.anyString()) ).thenReturn(true);

        // Execução
        Throwable exception = Assertions.catchThrowable( () -> service.save(book)) ;

        // Verificaçoes
        assertThat(exception).isInstanceOf(BussinessException.class)
            .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);
    }



    private Book createValidBook() {
        return Book.builder().author("Uncle Bob").isbn("123").title("Clean Code").build();
    }
}
