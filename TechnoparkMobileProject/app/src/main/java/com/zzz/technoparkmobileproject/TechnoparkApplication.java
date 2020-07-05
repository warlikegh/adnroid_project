package com.zzz.technoparkmobileproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.my.tracker.MyTracker;
import com.my.tracker.MyTrackerConfig;
import com.my.tracker.MyTrackerParams;
import com.zzz.technoparkmobileproject.auth.AuthRepo;
import com.zzz.technoparkmobileproject.network.ApiRepo;

public class TechnoparkApplication extends Application {


    private ApiRepo mApiRepo;
    private AuthRepo mAuthRepo;
    static SharedPreferences mSecretSettings;
    static SharedPreferences.Editor mSecretEditor;
    public static String SALT = "salt";
    String ALL_DISCIPLINES;
    public static String CREATE_FIRST_SETTINGS = "create_first_settings";
    public static String IS_FIRST_NEWS = "is_first_news";
    public static String IS_FIRST_PROFILE = "is_first_profile";
    public static String IS_FIRST_SCHEDULE = "is_first_schedule";
    public static String AUTH_TOKEN = "auth_token";
    public static String LOGIN = "login";
    public static String PASSWORD = "password";
    public static String SITE = "site";
    public static String IS_DELETED = "is_deleted";
    public static String IS_AUTHORISED = "is_authorised";
    public static String TOKEN = " Token ";
    public static String GROUP_SEARCH = "group_search";
    public static String GROUP_ID = "group_ID"; // in change case you need change it in mobile_navigation.xml
    public static String GROUP_SETTINGS = "group_settings";
    public static String GROUP_LAST_ID = "group_last_ID";
    public static String GROUP_PATH_URL = "groups/";
    public static String NEWS_POS = "pos_news";
    public static String NEWS_P = "p";
    public static String NEWS_U1 = "ul";
    public static String NEWS_CODE = "code";
    public static String NEWS_O1 = "ol";
    public static String NEWS_BLOCKQOTE = "blockquote";
    public static String NEWS_H4 = "h4";
    public static String NEWS_H5 = "h5";
    public static String NEWS_H6 = "h6";
    public static String NEWS_PRE = "pre";
    public static String NEWS_IMG = "img";
    public static String DEFAULT_TWO_WEEK = "default_two_week";
    public static String DISCIPLINE = "discipline";
    public static String PROFILE_ID = "profile_ID"; // in change case you need change it in mobile_navigation.xml
    public static String PROFILE_MY_ID = "profile_my_id";
    public static String PROFILE_USERNAME = "profile_username"; // in change case you need change it in mobile_navigation.xml
    public static String FIREBASE_SETTINGS = "fireBase_settings";
    public static String FIREBASE_TOKEN = "fireBase_token";
    public static String FIREBASE_ID = "fireBase_ID";
    final public static String AUTH_PATH_URL = "auth/";
    final public static String NEWS_PATH_URL = "topics/subscribed/";
    final public static String PROFILE_PATH_URL = "profile/";
    final public static String PUSH_PATH_URL = "device_token/";
    final public static String SCHEDULE_PATH_URL = "schedule/";
    final public static String CHECK_PATH_URL = "/check/";
    final public static String AUTHORISATION = "Authorization";
    public static @NonNull String SDK_KEY = "99201576151741145914";

    @Override
    public void onCreate() {
        super.onCreate();
        ALL_DISCIPLINES = getResources().getString(R.string.all_disciplines);
        mSecretSettings = new SecretData().getSecretData(getApplicationContext());
        mSecretEditor = mSecretSettings.edit();
        mSecretEditor.putString(SALT, "").apply();

        mApiRepo = new ApiRepo(getApplicationContext());
        mAuthRepo = new AuthRepo(mApiRepo);

        SharedPreferences mSettings = getApplicationContext().getSharedPreferences(CREATE_FIRST_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_FIRST_NEWS, true)
                .putBoolean(IS_FIRST_PROFILE, true)
                .putBoolean(DEFAULT_TWO_WEEK, true)
                .putString(DISCIPLINE, ALL_DISCIPLINES);
        editor.apply();

        // При необходимости, настройте конфигурацию трекера
        // MyTrackerParams trackerParams = MyTracker.getTrackerParams();
        MyTrackerConfig trackerConfig = MyTracker.getTrackerConfig();
        // …
        // Настройте параметры трекера
        // …
        trackerConfig.setLaunchTimeout(10);
        trackerConfig.setRegion(MyTrackerConfig.Region.RU);
        // Инициализируйте трекер
        MyTracker.initTracker(SDK_KEY, this);
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
