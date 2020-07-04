package com.zzz.technoparkmobileproject.ui.shedule;


import android.annotation.SuppressLint;
import android.content.Context;

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
                final String mSuperShortTitle, final String mDate, final String mStartTime, final String mEndTime,
                final List<UserSchedule.Group> mGroups, final String mLocation,
                final Boolean mCheckingOpened, final Boolean mAttended, final String mFeedbackUrl) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                insertRoom(key, mTitle, mDiscipline, mShortTitle, mSuperShortTitle, mDate,
                        mStartTime, mEndTime, mGroups, mLocation, mCheckingOpened, mAttended, mFeedbackUrl);
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
                            String mStartTime, String mEndTime, List<UserSchedule.Group> mGroups, String mLocation,
                            Boolean mCheckingOpened, Boolean mAttended, String mFeedbackUrl) {
        Schedule schedule = new Schedule(key, mTitle, mDiscipline, mShortTitle, mSuperShortTitle, mDate,
                mStartTime, mEndTime, mGroups, mLocation, mCheckingOpened, mAttended, mFeedbackUrl);
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