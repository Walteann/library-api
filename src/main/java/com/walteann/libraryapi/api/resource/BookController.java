package com.walteann.libraryapi.api.resource;

import com.walteann.libraryapi.api.dto.BookDTO;
import com.walteann.libraryapi.api.dto.LoanDTO;
import com.walteann.libraryapi.model.entity.Book;
import com.walteann.libraryapi.model.entity.Loan;
import com.walteann.libraryapi.service.BookService;
import com.walteann.libraryapi.service.LoanService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Api("Book API")
public class BookController {

    private final BookService service;
    private final ModelMapper modelMapper;
    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates a book")
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        /*
        * O ModelMapper é uma biblioteca para melhorar o codigo abaixo.
        * Como era precisa transformar um BookDTO em um BOOK e depois o BooK em BookDTO
        * Book entity = Book.builder().author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn()).build();
        * */
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Obtains a book details by id")
    public BookDTO get(@PathVariable Long id) {
        return service.getById(id)
            .map(book -> modelMapper.map(book, BookDTO.class))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("delete a book by id")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Book succesfully deleted")
    })
    public void delete(@PathVariable Long id) {

        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        service.delete(book);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("updates a book")
    public BookDTO put(@PathVariable Long id, @RequestBody @Valid BookDTO dto) {

        return service.getById(id).map(book -> {

            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            service.update(book);
            return modelMapper.map(book, BookDTO.class);

        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
    }

    @GetMapping
    @ApiOperation("Find books by params")
    public Page<BookDTO> find(BookDTO dto, Pageable pageRequest) {
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = service.find(filter, pageRequest);

        List<BookDTO> list = result.getContent()
            .stream()
            .map( entity -> modelMapper.map(entity, BookDTO.class))
            .collect(Collectors.toList());

            return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }

    @GetMapping("{id}/loans")
    @ApiOperation("Obtains loans by id")
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {
        Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<Loan> result = loanService.getLoansByBook(book, pageable);

        List<LoanDTO> list = result.getContent()
                .stream()
                .map(loan -> {
                    Book loanBook = loan.getBook();
                    BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
    }

}
