package com.zzz.technoparkmobileproject.ui.profile;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM Profile")
    List<Profile> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);

    @Insert
    void insertMany(Profile... profile);

    @Delete
    void deleteMany(Profile... profile);

    @Delete
    void delete(Profile profile);

    @Query("SELECT * FROM Profile WHERE id = :id")
    Profile getById(long id);

}
