package com.example.technoparkmobileproject.ui.news;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.technoparkmobileproject.network.ApiRepo;
import com.example.technoparkmobileproject.network.NewsApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsRepo {
   // private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private final static MutableLiveData<List<UserNews.Result>> mNews = new MutableLiveData<>();


    static {
        mNews.setValue(Collections.<UserNews.Result>emptyList());
    }

    private final Context mContext;
    private NewsApi mNewsApi;

    public NewsRepo(Context context) {
        mContext = context;
        mNewsApi = ApiRepo.from(mContext).getNewsApi();
    }

    public LiveData<List<UserNews.Result>> getLessons() {
        return mNews;
    }

    public void refresh() {
        mNewsApi.getUserNews(" Token ").enqueue(new Callback<NewsApi.UserNewsPlain>() {
            @Override
            public void onResponse(Call<NewsApi.UserNewsPlain> call,
                                   Response<NewsApi.UserNewsPlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsApi.UserNewsPlain.Result> result = response.body().results;
                    mNews.postValue(transform(result));
                }
            }

            @Override
            public void onFailure(Call<NewsApi.UserNewsPlain> call, Throwable t) {
                Log.e("NewsRepo", "Failed to load", t);
            }
        });
    }

    private static List<UserNews.Result> transform(List<NewsApi.UserNewsPlain.Result> plains) {
        List<UserNews.Result> result = new ArrayList<>();
        for (NewsApi.UserNewsPlain.Result newsPlain : plains) {
            try {
                UserNews.Result news = map(newsPlain);
                result.add(news);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static UserNews.Author transformAuthor(NewsApi.UserNewsPlain.Author plains) {
        UserNews.Author author = null;

            try {
                author = mapAuthor(plains);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return author;
    }
    private static List<UserNews.Text> transformText(List<NewsApi.UserNewsPlain.Text> plains) {
        List<UserNews.Text> text = new ArrayList<>();
        for (NewsApi.UserNewsPlain.Text textPlain : plains) {
            try {
                UserNews.Text lesson = mapText(textPlain);
                text.add(lesson);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    private static UserNews.Result map(NewsApi.UserNewsPlain.Result resultPlain) throws ParseException {
        UserNews temp = new UserNews();
        UserNews.Author author=transformAuthor(resultPlain.author);
        List<UserNews.Text> text=transformText(resultPlain.text);
        UserNews.Result result = temp.new Result(
                author,
                resultPlain.blog,
                resultPlain.title,
                resultPlain.rating,
                resultPlain.publishDate,
                text,
                resultPlain.commentsCount);
        // sSimpleDateFormat.parse(lessonPlain.date)
        return result;
    }

    private static UserNews.Author mapAuthor(NewsApi.UserNewsPlain.Author authorPlain) throws ParseException {
        UserNews temp = new UserNews();
        UserNews.Author author = temp.new Author(
                authorPlain.fullname,
                authorPlain.avatarUrl,
                authorPlain.username,
                authorPlain.id
        );
        return author;
    }

    private static UserNews.Text mapText(NewsApi.UserNewsPlain.Text textPlain) throws ParseException {
        UserNews temp = new UserNews();
        UserNews.Text text = temp.new Text(
                textPlain.type,
                textPlain.content
        );
        return text;
    }
}