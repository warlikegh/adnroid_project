package com.example.technoparkmobileproject.network;

import android.content.Context;

import com.example.technoparkmobileproject.TechnoparkApplication;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRepo {
    private final OkHttpClient mOkHttpClient;
    private String[] BASE_URL = new String[]{
            "https://park.mail.ru/api/mobile/v1/",
            "https://sphere.mail.ru/api/mobile/v1/",
            "https://track.mail.ru/api/mobile/v1/",
            "https://polis.mail.ru/api/mobile/v1/",
            "https://technoatom.mail.ru/api/mobile/v1/",
            "https://vgu.mail.ru/api/mobile/v1/",
            "https://pgu.mail.ru/api/mobile/v1/",
            "https://pm.mail.ru/api/mobile/v1/",
            "https://data.mail.ru/api/mobile/v1/"
    };
    private Retrofit[] retrofits = new Retrofit[]{null, null, null, null, null, null, null, null, null};
    Integer SITES_COUNT = 9;

    public ApiRepo(Context context) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        HttpInterceptor mInterceptor = new HttpInterceptor(context);

        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .addInterceptor(mInterceptor)
                .build();

        for (int i = 0; i < SITES_COUNT; i++) {
            retrofits[i] = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL[i])
                    .client(mOkHttpClient)
                    .build();
        }
    }

    public AuthApi getAuthApi(int index) {
        return retrofits[index].create(AuthApi.class);
    }

    public NewsApi getNewsApi(int index) {
        return retrofits[index].create(NewsApi.class);
    }

    public ScheduleApi getScheduleApi(int index) {
        return retrofits[index].create(ScheduleApi.class);
    }

    public CheckApi getCheckApi(int index) {
        return retrofits[index].create(CheckApi.class);
    }

    public ProfileApi getProfileApi(int index) {
        return retrofits[index].create(ProfileApi.class);
    }

    public PushApi getPushApi(int index) {
        return retrofits[index].create(PushApi.class);
    }

    public GroupApi getGroupApi(int index) {
        return retrofits[index].create(GroupApi.class);
    }

    public static ApiRepo from(Context context) {
        return TechnoparkApplication.from(context).getApis();
    }

    public String getBaseURL(int pos) {
        return BASE_URL[pos % 9];
    }
}

