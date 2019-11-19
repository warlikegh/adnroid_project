package com.example.technoparkmobileproject.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.technoparkmobileproject.TechnoparkApplication;
import com.example.technoparkmobileproject.auth.AuthActivity;
import com.example.technoparkmobileproject.auth.AuthRepo;
import com.example.technoparkmobileproject.auth.AuthViewModel;

import java.io.IOException;
import java.security.GeneralSecurityException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRepo {
    private final AuthApi mAuthApi;
    private final NewsApi mNewsApi;
    private final ScheduleApi mScheduleApi;
    private final CheckApi mCheckApi;
    private final ProfileApi mProfileApi;
    private final PushApi mPushApi;
    private final OkHttpClient mOkHttpClient;
    private String BASE_URL="https://park.mail.ru/api/mobile/v1/";
    static String LOGIN = "login";
    static String PASSWORD = "password";
    static SharedPreferences mSettings;

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
    private class HttpInterceptor implements Interceptor {

        private Context mContext;

        public HttpInterceptor(Context context){
            mContext=context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            request = builder.build();
            Response response = chain.proceed(request);

            if (response.code() == 401) { //if unauthorized
                String masterKeyAlias = null;
                try {
                    masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mSettings = EncryptedSharedPreferences.create(
                            "secret_shared_prefs",
                            masterKeyAlias,
                            mContext,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    );
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final MediatorLiveData<AuthViewModel.AuthState> mAuthState = new MediatorLiveData<>();
                final LiveData<AuthRepo.AuthProgress> progressLiveData = AuthRepo.getInstance(mContext)
                        .login(mSettings.getString(LOGIN,""), mSettings.getString(PASSWORD,""));
            } else if (response.code()==400){
                mContext.startActivity(new Intent(mContext, AuthActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

            return response;
        }
    }
}

