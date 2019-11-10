package com.example.technoparkmobileproject;

import android.app.Application;
import android.content.Context;

import com.example.technoparkmobileproject.auth.AuthRepo;
import com.example.technoparkmobileproject.network.ApiRepo;

public class TechnoparkApplication extends Application {


    private ApiRepo mApiRepo;
    private AuthRepo mAuthRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRepo = new ApiRepo();
        mAuthRepo = new AuthRepo(mApiRepo);
    }

    public AuthRepo getAuthRepo() {
        return mAuthRepo;
    }

    public ApiRepo getApis() {
        return mApiRepo;
    }

    public static TechnoparkApplication from(Context context) {
        return (TechnoparkApplication) context.getApplicationContext();
    }
}
