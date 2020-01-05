package com.example.technoparkmobileproject.group;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GroupViewModel extends AndroidViewModel {

    private GroupRepo mRepo = new GroupRepo(getApplication());
    private LiveData<UserGroup> mGroup = mRepo.getGroup();

    public GroupViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<UserGroup> getNews() {
        return mGroup;
    }

    public void refresh(Integer id) {
        mRepo.refresh(id);
    }

    public void pullFromDB(Integer id) {
        mRepo.pullFromDB(id);
    }

}