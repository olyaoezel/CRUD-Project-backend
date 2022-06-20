package com.example.demo.db;

import com.example.demo.model.Book;
import com.example.demo.model.BookGenres;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Page<Book> findByBookGenres(BookGenres bookGenres, Pageable pageable);
    Page<Book> findAllByNameContains(String name, Pageable pageable);
}

