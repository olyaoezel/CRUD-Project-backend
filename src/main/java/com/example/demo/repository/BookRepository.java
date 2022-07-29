package com.example.demo.repository;

import com.example.demo.model.Book;
import com.example.demo.model.BookGenres;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Page<Book> findByBookGenres(BookGenres bookGenres, Pageable pageable);
    Page<Book> findAllByNameContains(String name, Pageable pageable);
    Page<Book> findByUserId(Long userId, Pageable pageable); // gets a list of all books which belong to the current user
    Page<Book> findByIdIn(List<Long> ids, Pageable pageable); //it finds books by genre and which belong to the current user
//    Page<Book> findByNameContainsIn(List, Pageable pageable);
}

