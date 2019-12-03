package com.example.technoparkmobileproject.ui.news;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "News")
public class News {

    @PrimaryKey
    public int id;

    public String title;
    public String authorName;
    public String authorUrl;
    public String authorAva;
    Integer commentsCount;
    public String publishDate;
    Double rating;
/*    public List<String> contentText;
    public List<String> typeText;*/
    public String contentShort;
    public String typeShort;
    public String url;


    public News() {
    }

    public News(int key) {
        id = key;

    }
}
