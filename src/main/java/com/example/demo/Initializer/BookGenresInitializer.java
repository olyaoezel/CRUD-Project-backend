package com.example.demo.Initializer;

import com.example.demo.repository.BookGenresRepository;
import com.example.demo.model.BookGenres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookGenresInitializer {

    @Autowired
    private BookGenresRepository bookGenresRepository;


   @PostConstruct
    public void init(){
       if (bookGenresRepository.count() == 0) {
           List<String> genres = Arrays.asList("Suspense and Thriller", "Horror", "Romance", "Historical", "Sci-Fi", "Classics", "Fairy Tale", "Detective and Mystery", "Fantasy", "Action and Adventure", "Crime", "Drama", "Poetry", "Biography and Autobiography", "Cooking", "Memoir" );
           bookGenresRepository.saveAll(genres.stream().map(BookGenres::new).collect(Collectors.toList()));
           BookGenres genre = bookGenresRepository.findByGenre("Horror").get();
            genre.getBooks();
        }
    }
}
