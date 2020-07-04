package com.zzz.technoparkmobileproject.ui.profile;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Profile.class}, version = 1, exportSchema = false)
public abstract class ProfileDataBase extends RoomDatabase {

    public abstract ProfileDao getProfileDao();

    private static ProfileDataBase instance;

    static synchronized ProfileDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static ProfileDataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                ProfileDataBase.class,
                "my_db_name_profile").build();
    }

}
