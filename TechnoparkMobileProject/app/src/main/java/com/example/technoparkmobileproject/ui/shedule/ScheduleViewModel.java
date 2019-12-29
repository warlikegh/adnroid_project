package com.example.technoparkmobileproject.ui.shedule;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel {

    private ScheduleRepo mRepo = new ScheduleRepo(getApplication());
    private LiveData<List<UserSchedule>> mSchedule = mRepo.getSchedule();

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<UserSchedule>> getSchedule() {
        return mSchedule;
    }

    public void refresh() {
        mRepo.refresh();
    }

    public void pullFromDB() {
        mRepo.pullFromDB();
    }

    public void cleanDB() {
        mRepo.cleanDB();
    }

}