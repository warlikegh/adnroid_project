package com.example.technoparkmobileproject.network;

import android.content.Context;
import android.util.Log;

import com.example.technoparkmobileproject.TechnoparkApplication;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRepo {
    private static final String TAG ="URL_Find" ;
    private final AuthApi mAuthApi;
    private final NewsApi mNewsApi;
    private final ScheduleApi mScheduleApi;
    private final CheckApi mCheckApi;
    private final ProfileApi mProfileApi;
    private final PushApi mPushApi;
    private final OkHttpClient mOkHttpClient;
    private String BASE_URL="https://park.mail.ru/api/mobile/v1/";

    public ApiRepo(Context context) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        HttpInterceptor mInterceptor = new HttpInterceptor(context);

        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .addInterceptor(mInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .build();

        mAuthApi = retrofit.create(AuthApi.class);
        mNewsApi = retrofit.create(NewsApi.class);
        mScheduleApi = retrofit.create(ScheduleApi.class);
        mCheckApi = retrofit.create(CheckApi.class);
        mProfileApi = retrofit.create(ProfileApi.class);
        mPushApi = retrofit.create(PushApi.class);
    }

    public void setUrl(String url) {
        BASE_URL = url;
        Log.d(TAG,url);
    }

    public AuthApi getAuthApi() {
        return mAuthApi;
    }

    public NewsApi getNewsApi() {
        return mNewsApi;
    }

    public ScheduleApi getScheduleApi() {
        return mScheduleApi;
    }

    public CheckApi getCheckApi() {
        return mCheckApi;
    }

    public ProfileApi getProfileApi() {
        return mProfileApi;
    }

    public PushApi getPushApi() {
        return mPushApi;
    }

    public static ApiRepo from(Context context) {

        return TechnoparkApplication.from(context).getApis();
    }
}

