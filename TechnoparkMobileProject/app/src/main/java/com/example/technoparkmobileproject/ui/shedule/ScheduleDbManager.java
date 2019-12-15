package com.example.technoparkmobileproject.ui.shedule;


import android.annotation.SuppressLint;
import android.content.Context;

import com.example.technoparkmobileproject.network.ScheduleApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class ScheduleDbManager {

    @SuppressLint("StaticFieldLeak")
    private static final ScheduleDbManager INSTANCE = new ScheduleDbManager();

    static ScheduleDbManager getInstance(Context context) {
        INSTANCE.context = context.getApplicationContext();
        return INSTANCE;
    }

    private final Executor executor = Executors.newSingleThreadExecutor();

    private Context context;

    void insert(final int key, final String mTitle, final String mDiscipline, final String mShortTitle,
                final String mSuperShortTitle, final String mDate, final String mStartTime, final String  mEndTime,
                final List<ScheduleApi.UserSchedulePlain.Group> mGroups, final String mLocation) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                insertRoom(key, mTitle, mDiscipline, mShortTitle, mSuperShortTitle, mDate,
                        mStartTime, mEndTime, mGroups, mLocation);
            }
        });
    }

    void readAll(final ReadAllListener<Schedule> listener) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                readAllRoom(listener);
            }
        });
    }

    void clean() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cleanRoom();
            }
        });
    }

    private void cleanRoom() {
        ScheduleDao ScheduleDao = ScheduleDataBase.getInstance(context).getScheduleDao();
        ScheduleDao.deleteMany(ScheduleDao.getAll().toArray(new Schedule[0]));
    }

    private void insertRoom(int key, String mTitle, String mDiscipline, String mShortTitle, String mSuperShortTitle, String mDate,
                            String mStartTime, String  mEndTime, List<ScheduleApi.UserSchedulePlain.Group> mGroups, String mLocation) {
        Schedule schedule = new Schedule(key, mTitle, mDiscipline, mShortTitle, mSuperShortTitle, mDate,
                mStartTime, mEndTime, mGroups, mLocation);
        ScheduleDataBase.getInstance(context).getScheduleDao().insert(schedule);
    }

    private void readAllRoom(final ReadAllListener<Schedule> listener) {
        List<Schedule> list = ScheduleDataBase.getInstance(context).getScheduleDao().getAll();
        ArrayList<Schedule> strings = new ArrayList<>();
        for (Schedule schedule : list) {
            strings.add(schedule);
        }
        listener.onReadAll(strings);
    }

    public interface ReadAllListener<T> {
        void onReadAll(final Collection<T> allItems);
    }
}