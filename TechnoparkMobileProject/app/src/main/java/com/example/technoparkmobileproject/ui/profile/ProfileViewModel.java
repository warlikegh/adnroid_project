package com.example.technoparkmobileproject.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepo mRepo = new ProfileRepo(getApplication());
    private LiveData<UserProfile> mProfile = mRepo.getProfile();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<UserProfile> getProfile() {
        return mProfile;
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