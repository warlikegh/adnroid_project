package com.example.technoparkmobileproject.network;

import android.content.Context;

import com.example.technoparkmobileproject.TechnoparkApplication;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRepo {
    private final AuthApi mAuthApi;
    private final NewsApi mNewsApi;
    private final OkHttpClient mOkHttpClient;
    private String BASE_URL="https://park.mail.ru/api/mobile/v1/";

    public ApiRepo() {
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .build();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .client(client.build())
                .build();

        mAuthApi = retrofit.create(AuthApi.class);
        mNewsApi = retrofit.create(NewsApi.class);
    }

    public void setUrl(String url) {
        BASE_URL = url;
    }

    public AuthApi getAuthApi() {
        return mAuthApi;
    }

    public NewsApi getNewsApi() {
        return mNewsApi;
    }

    public static ApiRepo from(Context context) {
        return TechnoparkApplication.from(context).getApis();
    }
}

