package com.example.technoparkmobileproject.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.technoparkmobileproject.network.NewsApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DbManager {

    @SuppressLint("StaticFieldLeak")
    private static final DbManager INSTANCE = new DbManager();

    static DbManager getInstance(Context context) {
        INSTANCE.context = context.getApplicationContext();
        return INSTANCE;
    }

    private final Executor executor = Executors.newSingleThreadExecutor();

    private Context context;

    void insert(final int key, final String mTitle, final String mBlog, final String mAuthorName, final Integer mAuthorId, final String mAuthorAva,
                final Integer mCommentsCount, final String mPublishDate, final Double mRating,
                final List<NewsApi.UserNewsPlain.Text> mText, final List<NewsApi.UserNewsPlain.TextShort> mTextShort,
                final String mUrl, final String mNext) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                insertRoom(key, mTitle, mBlog, mAuthorName, mAuthorId, mAuthorAva,
                        mCommentsCount, mPublishDate, mRating, mText, mTextShort, mUrl, mNext);
            }
        });
    }

    void readAll(final ReadAllListener<News> listener) {
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

    private void insertRoom(int key, String mTitle, String mBlog, String mAuthorName, Integer mAuthorId, String mAuthorAva,
                            Integer mCommentsCount, String mPublishDate, Double mRating, List<NewsApi.UserNewsPlain.Text> mText,
                            List<NewsApi.UserNewsPlain.TextShort> mTypeShort, String mUrl, String mNext) {
        News news = new News(key, mTitle, mBlog, mAuthorName, mAuthorId, mAuthorAva, mCommentsCount,
                mPublishDate, mRating, mText, mTypeShort, mUrl, mNext);
        AppDataBase.getInstance(context).getNewsDao().insert(news);
    }

    private void readAllRoom(final ReadAllListener<News> listener) {
        List<News> list = AppDataBase.getInstance(context).getNewsDao().getAll();
        ArrayList<News> strings = new ArrayList<>();
        for (News news : list) {
              strings.add(news);
        }
        listener.onReadAll(strings);
    }

    public interface ReadAllListener<T> {
        void onReadAll(final Collection<T> allItems);
    }
}