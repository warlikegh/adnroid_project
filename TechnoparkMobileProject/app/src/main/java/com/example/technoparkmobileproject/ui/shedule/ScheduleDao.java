package com.example.technoparkmobileproject.ui.shedule;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM Schedule")
    List<Schedule> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Schedule news);

    @Insert
    void insertMany(Schedule... news);

    @Delete
    void deleteMany(Schedule... news);
}
