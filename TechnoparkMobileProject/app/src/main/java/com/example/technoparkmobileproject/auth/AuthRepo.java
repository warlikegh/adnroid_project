package com.example.technoparkmobileproject.auth;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.TechnoparkApplication;
import com.example.technoparkmobileproject.network.ApiRepo;
import com.example.technoparkmobileproject.network.AuthApi;
import com.example.technoparkmobileproject.network.PushApi;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.TELEPHONY_SERVICE;

@SuppressWarnings("WeakerAccess")
public class AuthRepo {

    private final ApiRepo mApiRepo;
    static SharedPreferences mSettings;
    static SharedPreferences.Editor editor;
    static String SALT = "salt";
    static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";
    private static String SITE = "site";
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
                                    .putInt(SITE, index).apply();

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

   /* private void registerAPN() {
        String pseudoID = "35" +
                Build.BOARD.length()%10 + Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10;

        PushApi api = mApiRepo.getPushApi(mSettings.getInt(SITE, -1));
        api.registerAPN(" Token " + mSettings.getString(AUTH_TOKEN, ""),
                new PushApi.UserPush(pseudoID,""))
                .enqueue(new Callback<PushApi.PushSuccess>() {
                    @Override
                    public void onResponse(Call<PushApi.PushSuccess> call,
                                           Response<PushApi.PushSuccess> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PushApi.PushSuccess user = response.body();
                            Log.d("push", user.getMessage());

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<PushApi.PushSuccess> call, Throwable t) {

                    }
                });
    }*/

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

}
