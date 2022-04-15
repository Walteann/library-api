package com.walteann.libraryapi.api.exception;

import com.walteann.libraryapi.exception.BussinessException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();

        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(BussinessException ex) {
        this.errors = Arrays.asList(ex.getMessage());
//        this.errors = new ArrayList<>();
//        this.errors.add(ex.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}