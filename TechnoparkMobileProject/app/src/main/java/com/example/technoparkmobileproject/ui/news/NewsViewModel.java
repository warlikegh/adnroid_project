package com.example.technoparkmobileproject.ui.news;

import android.app.Application;
import android.util.Log;

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

    public void refresh(String url) {
        mRepo.refresh(url);
        Log.e("okhttp3",url);
    }
}
