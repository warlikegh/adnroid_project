package com.example.technoparkmobileproject.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.technoparkmobileproject.auth.AuthActivity;
import com.example.technoparkmobileproject.auth.AuthRepo;
import com.example.technoparkmobileproject.auth.AuthViewModel;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.technoparkmobileproject.auth.AuthRepo.sha256;

public class HttpInterceptor implements Interceptor {

    static SharedPreferences.Editor editor;
    static SharedPreferences mSettings;
    static String LOGIN = "login";
    static String PASSWORD = "password";
    private Context mContext;
    static String SALT="salt";
    static String AUTH_TOKEN = "auth_token";

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
            editor = mSettings.edit();
            final ApiRepo mApiRepo;
            mApiRepo=ApiRepo.from(mContext);
            final String login=mSettings.getString(LOGIN,"");
            final String pass = mSettings.getString(PASSWORD,"");
            AuthApi api = mApiRepo.getAuthApi();
            String req=new BigInteger(16 * 4, new Random()).toString(16);
            api.getAuth(new AuthApi.ProfileAuth(login, pass, req, sha256(req+mSettings.getString(SALT,""))))
                    .enqueue(new Callback<AuthApi.UserAuth>() {
                        @Override
                        public void onResponse(Call<AuthApi.UserAuth> call,
                                               retrofit2.Response<AuthApi.UserAuth> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                AuthApi.UserAuth user = response.body();
                                editor.putString(AUTH_TOKEN, user.getAuthToken()).apply();
                                editor.putString(LOGIN, login).apply();
                                editor.putString(PASSWORD, pass).apply();

                            } else {
                                mContext.startActivity(new Intent(mContext, AuthActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthApi.UserAuth> call, Throwable t) {

                        }
                    });
             /*final MediatorLiveData<AuthViewModel.AuthState> mAuthState = new MediatorLiveData<>();
            final LiveData<AuthRepo.AuthProgress> progressLiveData = AuthRepo.getInstance(mContext)
                    .login(mSettings.getString(LOGIN,""), mSettings.getString(PASSWORD,""));
               mAuthState.addSource(progressLiveData, new Observer<AuthRepo.AuthProgress>() {
                    @Override
                    public void onChanged(AuthRepo.AuthProgress authProgress) {
                        if (authProgress == AuthRepo.AuthProgress.SUCCESS) {

                            mAuthState.removeSource(progressLiveData);
                        } else if (authProgress == AuthRepo.AuthProgress.FAILED) {
                            mContext.startActivity(new Intent(mContext, AuthActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            mAuthState.removeSource(progressLiveData);
                        }else if (authProgress == AuthRepo.AuthProgress.FAILED_NET) {

                            mAuthState.removeSource(progressLiveData);
                        }
                    }
                });*/
        }/* else if (response.code()==400){
            mContext.startActivity(new Intent(mContext, AuthActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }*/

        return response;
    }
}