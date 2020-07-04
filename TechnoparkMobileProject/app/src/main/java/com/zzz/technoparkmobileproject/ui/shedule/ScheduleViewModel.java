package com.zzz.technoparkmobileproject.ui.shedule;


import android.app.Application;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;

import com.zzz.technoparkmobileproject.network.CheckApi;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel {

    private ScheduleRepo mRepo = new ScheduleRepo(getApplication());
    private LiveData<List<UserSchedule>> mSchedule = mRepo.getSchedule();
    private LiveData<ScheduleRepo.ScheduleProgress> mScheduleProgress = mRepo.getScheduleProgress();
    private LiveData<CheckApi.UserCheck> mFeedback = mRepo.getFeedback();
    private LiveData<ScheduleRepo.CheckProgress> mCheckProgress = mRepo.getCheckProgress();

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<UserSchedule>> getSchedule() {
        return mSchedule;
    }

    public LiveData<ScheduleRepo.ScheduleProgress> getScheduleProgress() {
        return mScheduleProgress;
    }

    public LiveData<CheckApi.UserCheck> getFeedback() {
        return mFeedback;
    }

    public LiveData<ScheduleRepo.CheckProgress> getCheckProgress() {
        return mCheckProgress;
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

    public void check(Integer lessonID) {
        mRepo.check(lessonID);
    }

}