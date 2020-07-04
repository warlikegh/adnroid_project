package com.zzz.technoparkmobileproject.group;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GroupDao {

    @Query("SELECT * FROM Groups")
    List<Group> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Group group);

    @Insert
    void insertMany(Group... group);

    @Delete
    void deleteMany(Group... group);

    @Delete
    void delete(List<Group> group);

    @Query("DELETE FROM Groups WHERE idGroup = :id")
    void deleteByGroupId(long id);

    @Query("SELECT * FROM Groups WHERE idGroup = :id")
    List<Group> getById(long id);

}
