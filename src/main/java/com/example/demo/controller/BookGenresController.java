package com.example.demo.controller;

import com.example.demo.model.BookGenres;
import com.example.demo.db.BookGenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="genre")
public class BookGenresController {

    @Autowired
    BookGenresRepository bookGenresRepository;

    @GetMapping("/getAllGenres")
    public List<BookGenres> allGenres(){
      return bookGenresRepository.findAll();
  }

}
