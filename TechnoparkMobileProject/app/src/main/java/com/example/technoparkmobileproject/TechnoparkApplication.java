package com.example.technoparkmobileproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.technoparkmobileproject.auth.AuthRepo;
import com.example.technoparkmobileproject.network.ApiRepo;

public class TechnoparkApplication extends Application {


    private ApiRepo mApiRepo;
    private AuthRepo mAuthRepo;
    static SharedPreferences mSettings;
    static SharedPreferences.Editor editor;
    static String SALT = "salt";

    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = new SecretData().getSecretData(getApplicationContext());
        editor = mSettings.edit();
        editor.putString(SALT, "").apply();
        mApiRepo = new ApiRepo(getApplicationContext());
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
