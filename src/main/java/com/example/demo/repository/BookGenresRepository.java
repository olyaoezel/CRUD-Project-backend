package com.example.demo.db;

import com.example.demo.model.BookGenres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BookGenresRepository extends JpaRepository<BookGenres, Long> {

    Optional<BookGenres> findByGenre(String genre);
}
