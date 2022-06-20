package com.example.demo.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;


//@XmlRootElement
@Entity
@Table(name = "book_genres")

public class BookGenres {


    @OneToMany(mappedBy = "bookGenres", fetch = FetchType.LAZY, cascade =  CascadeType.PERSIST)
    //@Fetch(FetchMode.SELECT)
    //@JoinColumn(name = "genre", nullable = false, insertable=false, updatable=false)
//    @OnDelete(action = OnDeleteAction.CASCADE)

    private List<Book> books;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @OrderColumn(name = "genre", nullable = false)
    private String genre;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public BookGenres() {
    }

    public BookGenres(String genre){
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
