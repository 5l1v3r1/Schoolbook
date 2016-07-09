package com.marco.marplex.schoolbook.models;

/**
 * Created by marco on 30/06/16.
 */

public class Argument {
    public int iconID;
    public String subject;

    public String date, teacher, content;

    public Argument(String date, String teacher, String content) {
        this.date = date;
        this.teacher = teacher;
        this.content = content;
    }

    public Argument(int iconID, String subject) {
        this.iconID = iconID;
        this.subject = subject;
    }
}
