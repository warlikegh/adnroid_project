package com.zzz.technoparkmobileproject.ui.news;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class NewsDataBase extends RoomDatabase {

    public abstract NewsDao getNewsDao();

    private static NewsDataBase instance;

    static synchronized NewsDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static NewsDataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                NewsDataBase.class,
                "my_db_name").build();
    }
}
