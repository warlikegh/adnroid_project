package com.zzz.technoparkmobileproject.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zzz.technoparkmobileproject.MessagingService;
import com.zzz.technoparkmobileproject.SecretData;
import com.zzz.technoparkmobileproject.TechnoparkApplication;
import com.zzz.technoparkmobileproject.network.ApiRepo;
import com.zzz.technoparkmobileproject.network.AuthApi;
import com.zzz.technoparkmobileproject.network.PushApi;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.AUTH_TOKEN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_AUTHORISED;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_DELETED;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.LOGIN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PASSWORD;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SALT;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SITE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.TOKEN;

@SuppressWarnings("WeakerAccess")
public class AuthRepo {

    private final ApiRepo mApiRepo;
    static SharedPreferences mSettings;
    static SharedPreferences.Editor editor;
    private static Context context;

    public AuthRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    @NonNull
    public static AuthRepo getInstance(Context mContext) {
        context = mContext;
        mSettings = new SecretData().getSecretData(context);
        editor = mSettings.edit();
        return TechnoparkApplication.from(context).getAuthRepo();
    }

    private MutableLiveData<AuthProgress> mAuthProgress;

    public LiveData<AuthProgress> login(@NonNull String login, @NonNull String password, @NonNull Integer index) {
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);
        login(mAuthProgress, login, password, index);
        return mAuthProgress;
    }

    private void login(final MutableLiveData<AuthProgress> progress,
                       @NonNull final String login,
                       @NonNull final String password,
                       @NonNull final Integer index) {
        AuthApi api = mApiRepo.getAuthApi(index);

        String req = new SecretData().req();
        api.getAuth(new AuthApi.ProfileAuth(login, password, req, sha256(req + mSettings.getString(SALT, ""))))
                .enqueue(new Callback<AuthApi.UserAuth>() {
                    @Override
                    public void onResponse(Call<AuthApi.UserAuth> call,
                                           Response<AuthApi.UserAuth> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AuthApi.UserAuth user = response.body();
                            editor.putString(AUTH_TOKEN, user.getAuthToken())
                                    .putString(LOGIN, login)
                                    .putString(PASSWORD, password)
                                    .putInt(SITE, index)
                                    .putBoolean(IS_AUTHORISED, true).apply();

                            if (!mSettings.getBoolean(IS_DELETED, true))
                                deleteTokenFromServer(context);
                            sendTokenToServer(context);

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

    public enum AuthProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        FAILED_NET
    }

    public static String sha256(String base) {                                          //Algorithm for SHA256
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void sendTokenToServer(Context context) {
        PushApi mPushApi = ApiRepo.from(context).getPushApi(new SecretData().getSecretData(context).getInt(SITE, 0));
        SharedPreferences mSettings = new SecretData().getSecretData(context);

        mPushApi.registerAPN(TOKEN + mSettings.getString(AUTH_TOKEN, ""), getUserPush(context))
                .enqueue(new Callback<PushApi.PushSuccess>() {
                    @Override
                    public void onResponse(Call<PushApi.PushSuccess> call,
                                           Response<PushApi.PushSuccess> response) {
                    }

                    @Override
                    public void onFailure(Call<PushApi.PushSuccess> call, Throwable t) {
                        Log.d("wasPushTokenRegister", "NoInternet");
                    }
                });

    }

    static PushApi.UserPush getUserPush(Context context) {
        String token = MessagingService.getToken(context);
        String id = MessagingService.getID(context);

        Log.d("wasPushTokenRegister", id);
        Log.d("wasPushTokenRegister", token);

        return (new PushApi.UserPush(id, token));
    }

    public static void deleteTokenFromServer(Context context) {
        PushApi mPushApi = ApiRepo.from(context).getPushApi(new SecretData().getSecretData(context).getInt(SITE, 0));
        final SharedPreferences mSettings = new SecretData().getSecretData(context);

        mPushApi.deleteToken(TOKEN + mSettings.getString(AUTH_TOKEN, ""), getUserToken(context))
                .enqueue(new Callback<PushApi.PushSuccess>() {
                    @Override
                    public void onResponse(Call<PushApi.PushSuccess> call,
                                           Response<PushApi.PushSuccess> response) {
                        if (response.isSuccessful())
                            mSettings.edit().putBoolean(IS_DELETED, true).apply();
                        else
                            mSettings.edit().putBoolean(IS_DELETED, true).apply();
                    }

                    @Override
                    public void onFailure(Call<PushApi.PushSuccess> call, Throwable t) {
                        mSettings.edit().putBoolean(IS_DELETED, false).apply();
                    }
                });
    }

    static PushApi.UserToken getUserToken(Context context) {
        String token = MessagingService.getToken(context);

        Log.d("wasPushTokenDelete", token);

        return (new PushApi.UserToken(token));
    }

}
