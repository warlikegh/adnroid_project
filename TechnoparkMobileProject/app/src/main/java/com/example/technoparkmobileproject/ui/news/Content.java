package com.example.technoparkmobileproject.ui.news;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Content")
public class Content {

    @PrimaryKey
    public int id;

    public String contentText;
    public String typeText;

    public Content() {
    }

    public Content(int key) {
        id = key;
    }
}
