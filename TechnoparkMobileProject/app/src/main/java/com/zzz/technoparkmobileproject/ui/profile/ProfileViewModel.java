package com.zzz.technoparkmobileproject.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepo mRepo = new ProfileRepo(getApplication());
    private LiveData<UserProfile> mProfile = mRepo.getProfile();
    private LiveData<ProfileRepo.ProfileProgress> mProfileProgress = mRepo.getProfileProgress();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<UserProfile> getProfile() {
        return mProfile;
    }

    public LiveData<ProfileRepo.ProfileProgress> getProfileProgress() {
        return mProfileProgress;
    }

    public void refreshMe() {
        mRepo.refreshMe();
    }

    public void pullMeFromDB() {
        mRepo.pullMeFromDB();
    }

    public void refresh(String username, long id) {
        mRepo.refresh(username, (int) id);
    }

    public void pullFromDB(long id, String username) {
        mRepo.pullFromDB(id, username);
    }

    public void cleanDB() {
        mRepo.cleanDB();
    }
}