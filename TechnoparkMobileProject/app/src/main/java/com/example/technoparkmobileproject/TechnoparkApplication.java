package com.example.technoparkmobileproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.technoparkmobileproject.auth.AuthRepo;
import com.example.technoparkmobileproject.network.ApiRepo;

public class TechnoparkApplication extends Application {


    private ApiRepo mApiRepo;
    private AuthRepo mAuthRepo;
    static SharedPreferences mSecretSettings;
    static SharedPreferences.Editor mSecretEditor;
    static String SALT = "salt";
    String ALL_DISCIPLINES;

    @Override
    public void onCreate() {
        super.onCreate();
        ALL_DISCIPLINES = getResources().getString(R.string.all_disciplines);
        mSecretSettings = new SecretData().getSecretData(getApplicationContext());
        mSecretEditor = mSecretSettings.edit();
        mSecretEditor.putString(SALT, "here must be salt").apply();

        mApiRepo = new ApiRepo(getApplicationContext());
        mAuthRepo = new AuthRepo(mApiRepo);

        SharedPreferences mSettings = getApplicationContext().getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("isFirstNews", true)
                .putBoolean("isFirstProfile", true)
                .putBoolean("default", true)
                .putString("discipline", ALL_DISCIPLINES);

        editor.apply();
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
