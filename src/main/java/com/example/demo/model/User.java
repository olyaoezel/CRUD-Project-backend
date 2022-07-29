package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user")

public class User implements Serializable {

    public User() {}

//    public User(String username, String password){
//        this.username = username;
//        this.password = password;
//    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user",  fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public String getUsername(){return username;}

    public void setUsername(String username){this.username = username;}

    public String getPassword(){ return password;}

    public void setPassword(String password){this.password = password;}

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}




