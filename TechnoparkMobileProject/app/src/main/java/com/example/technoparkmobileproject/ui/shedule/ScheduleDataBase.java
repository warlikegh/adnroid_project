package com.example.technoparkmobileproject.ui.shedule;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Schedule.class}, version = 1, exportSchema = false)
public abstract class ScheduleDataBase extends RoomDatabase {

    public abstract ScheduleDao getScheduleDao();

    private static ScheduleDataBase instance;

    static synchronized ScheduleDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }


    private static ScheduleDataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                ScheduleDataBase.class,
                "my_db_name_schedule").build();
    }
}
