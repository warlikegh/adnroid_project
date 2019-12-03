package com.example.technoparkmobileproject.ui.news;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContentDao {

    @Query("SELECT * FROM Content")
    List<Content> getAll();

    @Query("SELECT * FROM Content WHERE id = :id")
    Content getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Content content);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Content> contents);

    @Update
    void update(Content content);

    @Delete
    void delete(Content content);


}