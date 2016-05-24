package com.marco.marplex.schoolbook.models;

/**
 * Created by marco on 5/24/16.
 */

public class Comunication {
    public int id;
    public String title, date, link;
    public Comunication(int id, String title, String date, String link) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.link = link;
    }
}
