package com.example.marplex.schoolbook.models;

/**
 * Created by marco on 5/18/16.
 */
public class Library {
    public String author, name, url;
    public int image;
    public Library(String author, String name, int image, String url) {
        this.author = author;
        this.name = name;
        this.image = image;
        this.url = url;
    }
}
