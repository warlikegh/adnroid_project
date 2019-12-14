package com.example.technoparkmobileproject.ui.news;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;


public class NewsViewModel extends AndroidViewModel {

    private NewsRepo mRepo = new NewsRepo(getApplication());
    private LiveData<UserNews> mNews = mRepo.getNews();
    private LiveData<UserNews> mNextNews = mRepo.getNextNews();

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<UserNews> getNews() {
        return mNews;
    }

    public LiveData<UserNews> getNextNews() {
        return mNextNews;
    }

    public void refresh() {
        mRepo.refresh();
    }

    public void setNextNews(String url) {
        mRepo.setNextNews(url);
    }

    public void pullFromDB() {
        mRepo.pullFromDB();
    }
}
