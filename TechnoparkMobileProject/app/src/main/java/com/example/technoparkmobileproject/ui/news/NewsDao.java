package com.example.technoparkmobileproject.ui.news;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM News")
    List<News> getAll();

    @Query("SELECT * FROM News WHERE id = :id")
    News getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    @Update
    void update(News news);

    @Delete
    void delete(News news);

    @Insert
    void insertMany(News... news);

    @Delete
    void deleteMany(News... news);

}