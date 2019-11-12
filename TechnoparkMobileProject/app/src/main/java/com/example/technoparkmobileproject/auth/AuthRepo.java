package com.example.technoparkmobileproject.auth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.technoparkmobileproject.TechnoparkApplication;
import com.example.technoparkmobileproject.network.ApiRepo;
import com.example.technoparkmobileproject.network.AuthApi;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("WeakerAccess")
public class AuthRepo {

    private final ApiRepo mApiRepo;

    public AuthRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    @NonNull
    public static AuthRepo getInstance(Context context) {
        return TechnoparkApplication.from(context).getAuthRepo();
    }

    private MutableLiveData<AuthProgress> mAuthProgress;

    public LiveData<AuthProgress> login(@NonNull String login, @NonNull String password) {
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);
        login(mAuthProgress, login, password);
        return mAuthProgress;
    }

    private void login(final MutableLiveData<AuthProgress> progress, @NonNull final String login, @NonNull final String password) {
        AuthApi api = mApiRepo.getAuthApi();
        String req=new BigInteger(16 * 4, new Random()).toString(16);
        api.getAuth(new AuthApi.ProfileAuth(login,password,req, sha256(req+"")))
                .enqueue(new Callback<AuthApi.UserAuth>() {
            @Override
            public void onResponse(Call<AuthApi.UserAuth> call,
                                   Response<AuthApi.UserAuth> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthApi.UserAuth user = response.body();

                   /*Here we are saving data */

                    progress.postValue(AuthProgress.SUCCESS);
                } else {
                progress.postValue(AuthProgress.FAILED);
                }
            }

            @Override
            public void onFailure(Call<AuthApi.UserAuth> call, Throwable t) {
                progress.postValue(AuthProgress.FAILED_NET);
            }
        });
    }

    enum AuthProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        FAILED_NET
    }

    public static String sha256(String base) {                                          //Algorithm for SHA256
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
