package com.example.technoparkmobileproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.technoparkmobileproject.auth.AuthRepo;
import com.example.technoparkmobileproject.network.ApiRepo;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class TechnoparkApplication extends Application {


    private ApiRepo mApiRepo;
    private AuthRepo mAuthRepo;
    static SharedPreferences mSecretSettings;
    static SharedPreferences.Editor mSecretEditor;
    static String SALT = "salt";

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);

        mSecretSettings = new SecretData().getSecretData(getApplicationContext());
        mSecretEditor = mSecretSettings.edit();
        mSecretEditor.putString(SALT, "b3d0341e9c1b5b2b73fc84dc59de6ac9f81e2710154c780302915b8e9082b5ef").apply();

        mApiRepo = new ApiRepo(getApplicationContext());
        mAuthRepo = new AuthRepo(mApiRepo);

        SharedPreferences mSettings = getApplicationContext().getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("isFirstNews", true);
        editor.putBoolean("isFirstSchedule", true);
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
