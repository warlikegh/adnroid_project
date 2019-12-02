package com.example.technoparkmobileproject.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DbManager {

    private static final int VERSION = 1;

    @SuppressLint("StaticFieldLeak")
    private static final DbManager INSTANCE = new DbManager();

    private static final String TEXT_COLUMN = "TEXT_COLUMN";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String DB_NAME = "MyDatabase.db";

    static DbManager getInstance(Context context) {
        INSTANCE.context = context.getApplicationContext();
        return INSTANCE;
    }

    private final Executor executor = Executors.newSingleThreadExecutor();

    private Context context;

    void insert(final String text) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                insertRoom(text);
            }
        });
    }

    void readAll(final ReadAllListener<String> listener) {
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
        NewsDao newsDao = AppDataBase.getInstance(context).getNewsDao();
        newsDao.deleteMany(newsDao.getAll().toArray(new News[0]));
    }

    private void insertRoom(String text) {
        News news = new News();
        // news.text = text;
        AppDataBase.getInstance(context).getNewsDao().insertMany(news);
    }

    private void readAllRoom(final ReadAllListener<String> listener) {
        List<News> list = AppDataBase.getInstance(context).getNewsDao().getAll();
        ArrayList<String> strings = new ArrayList<>();
        for (News news : list) {
            //  strings.add(news.text);
        }
        listener.onReadAll(strings);
    }

    public interface ReadAllListener<T> {
        void onReadAll(final Collection<T> allItems);
    }
}