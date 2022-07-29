package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "book_table")
public class Book  {
    public Book() {
        super();
    }

    public Book(String name, String imageType, byte[] picByte, Long id) {
        this.id = id;
        this.name = name;
        this.imageType = imageType;
        this.picByte = picByte;
    }

    public Book(String name, String imageType, byte[] picByte) {
        this.name = name;
        this.imageType = imageType;
        this.picByte = picByte;
    }

    public Book( String name, String author, int year, String description, String imageType, byte[] picByte) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.description = description;
        this.imageType = imageType;
        this.picByte = picByte;
    }

    public Book( String name, String author, int year, String description, String imageType, byte[] picByte, Long id) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.description = description;
        this.imageType = imageType;
        this.picByte = picByte;
        this.id = id;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "year")
    private int year;

    @Column(name = "description",  columnDefinition="varchar(1000)")
    private String description;


    @Column(name = "imageType")
    private String imageType;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column

    @Column(name = "picByte", length = 16777215)
    private byte[] picByte;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_has_book", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private User user;


    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "genre")
    private BookGenres bookGenres;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BookGenres getBookGenres() {
        return bookGenres;
    }

    public void setBookGenres(BookGenres bookGenres) {
        this.bookGenres = bookGenres;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
