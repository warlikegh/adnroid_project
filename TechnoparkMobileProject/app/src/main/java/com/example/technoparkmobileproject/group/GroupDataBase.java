package com.example.technoparkmobileproject.group;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Group.class}, version = 1, exportSchema = false)
public abstract class GroupDataBase extends RoomDatabase {

    public abstract GroupDao getGroupDao();

    private static GroupDataBase instance;

    static synchronized GroupDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static GroupDataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                GroupDataBase.class,
                "my_db_name_group").build();
    }

}