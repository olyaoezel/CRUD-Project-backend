package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.model.Book;
import com.example.demo.model.BookGenres;
import com.example.demo.repository.BookGenresRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "book")

public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookGenresRepository bookGenresRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(value="/upload",  consumes = {  "multipart/form-data" }, produces = { MediaType.APPLICATION_JSON_VALUE }) //uploads the book info (Request part 'bookFile' is for the image)
    public ResponseEntity<?> uploadBook(@RequestPart("bookFile") @NotNull MultipartFile file,
                                        @RequestPart("book") String book,
                                        @RequestPart("genre") String genre) throws IOException {
    //  the data submitted as a JSON object, which needs to be parsed
        JSONObject bookJson = new JSONObject(book);

        Book newBook = new Book(bookJson.getString("name"), bookJson.getString("author"), bookJson.getInt("year"), bookJson.getString("description"),  file.getContentType(),
          file.getBytes());

        newBook.setBookGenres(bookGenresRepository.findByGenre(genre).get());

        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        newBook.setUser(userRepository.findByUsername(auth));

        bookRepository.save(newBook);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getByGenre/{genre}") // gets all books of the chosen by the user genre with a pagination and sorted by a certain criterion if specified
    public Page<Book> getByGenre(@PathVariable("genre") String genre,
                                 @RequestParam(defaultValue = "0") Integer pageNo,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        Optional<BookGenres> genreFound = bookGenresRepository.findByGenre(genre);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//        Page<Book> retrievedBooks = bookRepository.findByBookGenres(genreFound.get(), paging);


        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByUsername(auth).getId();
        Page<Book> pagedResult = bookRepository.findByUserId(userId, paging);
        List<Book> newList = pagedResult.stream().filter(book -> book.getBookGenres().getGenre().equalsIgnoreCase(genreFound.get().getGenre())).collect(Collectors.toList());
        List<Long> ids = new ArrayList<>();
        for (Book book : newList) ids.add(book.getId());
        Page<Book> listOfBooks = bookRepository.findByIdIn(ids, paging);

        return listOfBooks;
    }


    @GetMapping("/getById/{id}") //gets a book by its id and returns its info & its genre, needed for the input fields in the update modal to be pre-filled with the existing info
    public List<Object> getBookById(@PathVariable Long id) {
        Optional<Book> retrievedBook = bookRepository.findById(id);
        Book book = getRetrievedBookInfo(retrievedBook);
        List<Object> response = new ArrayList<>();
        response.add(book);
        response.add(retrievedBook.get().getBookGenres().getGenre());
        return response;
    }


    @GetMapping("/getAllBooks") // gets a list of all books which belong to the current user with a pagination and sorted by a certain criterion  if specified. Returns the items only for 1 page
    public Page<Book> getAllBooks(@RequestParam(defaultValue = "0") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "id") String sortBy)  {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByUsername(auth).getId();
        Page<Book> pagedResult = bookRepository.findByUserId(userId, paging);

       return pagedResult;
    }

    @GetMapping("/getAllLibraryBooks") // gets a list of all books from the library with a pagination and sorted by a certain criterion  if specified. Returns the items only for 1 page
    public Page<Book> getAllLibraryBooks(@RequestParam(defaultValue = "0") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "id") String sortBy)  {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Book> pagedResult = bookRepository.findAll(paging);

        return pagedResult;
    }

    @GetMapping("/getFilteredBooks") //gets the book/books which title contains the request from the search input. The output is with a pagination and sorted by a certain criterion  if specified
    public Page<Book> getFilteredBooks(@RequestParam(defaultValue = "0") Integer pageNo,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(defaultValue = "id") String sortBy,
                                       @RequestParam(name = "bookTitle") String bookTitle)  {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//        Page<Book> pagedResult = bookRepository.findAllByNameContains(bookTitle, paging);

        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByUsername(auth).getId();
        Page<Book> pagedResult = bookRepository.findByUserId(userId, paging);
        List<Book> newList = pagedResult.stream().filter(book -> book.getName().contains(bookTitle)).collect(Collectors.toList());
        List<Long> ids = new ArrayList<>();
        for (Book book : newList) ids.add(book.getId());
        Page<Book> listOfBooks = bookRepository.findByIdIn(ids, paging);

        return listOfBooks;
    }

    @PutMapping(value="/update/{id}", consumes = {  "multipart/form-data" }) // updates the book info or creates a new one if it doesn't exist yet due to an occurred error of retrieving one
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id,
                                        @RequestParam(name="bookFile", required = false) MultipartFile file,
                                        @RequestPart(name="book", required = false) String book,
                                        @RequestPart(name="genre", required = false) String genre) throws IOException {
        Optional<Book> retrievedBook = bookRepository.findById(id);
        JSONObject bookJson = new JSONObject(book);

        return retrievedBook.map(el -> {
            el.setName(bookJson.getString("name"));
            el.setAuthor(bookJson.getString("author"));
            el.setYear(bookJson.getInt("year"));
            el.setDescription(bookJson.getString("description"));
            el.setBookGenres(bookGenresRepository.findByGenre(genre).orElseThrow());

            try {
                if(file != null ){
                    el.setImageType(file.getContentType());
                    el.setPicByte(file.getBytes());
                }
                bookRepository.save(el);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }).orElseGet(() -> {

            try {
                Book el = new Book(bookJson.getString("name"), bookJson.getString("author"), bookJson.getInt("year"), bookJson.getString("description"),  file.getContentType(),
                        file.getBytes());
                el.setBookGenres(bookGenresRepository.findByGenre(genre).orElseThrow());
                bookRepository.save(el);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

    @DeleteMapping("/delete/{id}") // deletes a book by id
    void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

    //function for creating a book from a retrieved info
    Book getRetrievedBookInfo(@NotNull Optional<Book> retrievedBook){
        Book book = new Book(retrievedBook.get().getName(), retrievedBook.get().getAuthor(), retrievedBook.get().getYear(), retrievedBook.get().getDescription(), retrievedBook.get().getImageType(),
                retrievedBook.get().getPicByte(), retrievedBook.get().getId());
        return book;
    }

}


//file.getOriginalFilename().replaceFirst("[.][^.]+$", ""),
