package com.zzz.technoparkmobileproject.ui.news;

import android.app.Application;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;


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

    public void setNextNews(String url) {
        mRepo.setNextNews(url);
    }

    public void pullFromDB() {
        mRepo.pullFromDB();
    }

    public void openNews(int pos) {
        mRepo.openNews(pos);
    }

    ;
}
