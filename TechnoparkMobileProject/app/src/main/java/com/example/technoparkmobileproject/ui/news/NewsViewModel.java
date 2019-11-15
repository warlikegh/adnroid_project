package com.example.technoparkmobileproject.ui.news;

import android.app.Application;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    private NewsRepo mRepo = new NewsRepo(getApplication());
    private LiveData<UserNews> mNews = mRepo.getNews();

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<UserNews> getNews() {
        return mNews;
    }

    public void refresh() {
        mRepo.refresh();
    }
}
