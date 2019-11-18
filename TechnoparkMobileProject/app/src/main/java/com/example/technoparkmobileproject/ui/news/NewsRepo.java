package com.example.technoparkmobileproject.ui.news;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.technoparkmobileproject.network.ApiRepo;
import com.example.technoparkmobileproject.network.NewsApi;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class NewsRepo {
   // private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private final static MutableLiveData<UserNews> mNews = new MutableLiveData<>();
    private SharedPreferences mSettings;
    //SharedPreferences.Editor editor;
    private final Context mContext;
    private NewsApi mNewsApi;
    private static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";

    NewsRepo(Context context) {
        mContext = context;
        mNewsApi = ApiRepo.from(mContext).getNewsApi();

    }

    public LiveData<UserNews> getNews() {
        return mNews;
    }

    public void refresh() {
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        try {
            assert masterKeyAlias != null;
            mSettings = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    mContext,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        mNewsApi.getUserNews(" Token "+mSettings.getString(AUTH_TOKEN,"")).enqueue(new Callback<NewsApi.UserNewsPlain>() {
            @Override
            public void onResponse(Call<NewsApi.UserNewsPlain> call,
                                   Response<NewsApi.UserNewsPlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewsApi.UserNewsPlain result = response.body();
                    mNews.postValue(transform(result));
                } else {
                    if (response.code() == 401) {/*
                        final MediatorLiveData<AuthViewModel.AuthState> mAuthState = new MediatorLiveData<>();

                        final LiveData<AuthRepo.AuthProgress> progressLiveData = AuthRepo.getInstance(TechnoparkApplication.from(mContext))
                                .login(mSettings.getString(LOGIN, ""), mSettings.getString(PASSWORD, ""));
                        mAuthState.addSource(progressLiveData, new Observer<AuthRepo.AuthProgress>() {
                            @Override
                            public void onChanged(AuthRepo.AuthProgress authProgress) {
                                if (authProgress == AuthRepo.AuthProgress.SUCCESS) {
                                   refresh();
                                   mAuthState.removeSource(progressLiveData);
                                } else if (authProgress == AuthRepo.AuthProgress.FAILED) {

                                    mAuthState.removeSource(progressLiveData);
                                } else if (authProgress == AuthRepo.AuthProgress.FAILED_NET) {

                                    mAuthState.removeSource(progressLiveData);
                                }
                            }
                        });*/
                    }
                }
            }
            @Override
            public void onFailure(Call<NewsApi.UserNewsPlain> call, Throwable t) {
                Log.e("NewsRepo", "Failed to load", t);
            }
        });
    }

    private static UserNews transform(NewsApi.UserNewsPlain plains) {
        List<UserNews.Result> result = new ArrayList<>();
        for (NewsApi.UserNewsPlain.Result newsPlain : plains.results) {
            try {
                UserNews.Result news = map(newsPlain);
                result.add(news);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new UserNews(plains.count,plains.next,plains.previous,result);
    }


    private static List<UserNews.Text> transformText(List<NewsApi.UserNewsPlain.Text> plains) {
        List<UserNews.Text> texts = new ArrayList<>();
        for (NewsApi.UserNewsPlain.Text textPlain : plains) {
            try {
                UserNews.Text text = mapText(textPlain);
                texts.add(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return texts;
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


    private static UserNews.Result map(NewsApi.UserNewsPlain.Result resultPlain) throws ParseException {
        UserNews temp = new UserNews();
        UserNews.Author author=transformAuthor(resultPlain.author);
        List<UserNews.Text> text=transformText(resultPlain.text);
        // sSimpleDateFormat.parse(lessonPlain.date)
        return temp.new Result(
                author,
                resultPlain.blog,
                resultPlain.title,
                resultPlain.rating,
                resultPlain.publishDate,
                text,
                resultPlain.commentsCount);
    }

    private static UserNews.Author mapAuthor(NewsApi.UserNewsPlain.Author authorPlain) throws ParseException {
        UserNews temp = new UserNews();
        return temp.new Author(
                authorPlain.fullname,
                authorPlain.avatarUrl,
                authorPlain.username,
                authorPlain.id
        );
    }

    private static UserNews.Text mapText(NewsApi.UserNewsPlain.Text textPlain) throws ParseException {
        UserNews temp = new UserNews();
        return temp.new Text(
                textPlain.type,
                textPlain.content
        );
    }
}