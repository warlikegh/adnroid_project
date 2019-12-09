package com.example.technoparkmobileproject.ui.news;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM News")
    List<News> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    @Insert
    void insertMany(News... news);

    @Delete
    void deleteMany(News... news);

}