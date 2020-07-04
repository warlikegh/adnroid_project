package com.zzz.technoparkmobileproject.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zzz.technoparkmobileproject.SecretData;
import com.zzz.technoparkmobileproject.auth.AuthActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.AUTH_TOKEN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.LOGIN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PASSWORD;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SALT;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SITE;
import static com.zzz.technoparkmobileproject.auth.AuthRepo.sha256;

public class HttpInterceptor implements Interceptor {

    static SharedPreferences.Editor editor;
    static SharedPreferences mSettings;
    private Context mContext;

    public HttpInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        request = builder.build();
        Response response = chain.proceed(request);

        if (response.code() == 401) { //if unauthorized
            mSettings = new SecretData().getSecretData(mContext);
            editor = mSettings.edit();
            final ApiRepo mApiRepo;
            mApiRepo = ApiRepo.from(mContext);
            final String login = mSettings.getString(LOGIN, "");
            final String pass = mSettings.getString(PASSWORD, "");
            AuthApi api = mApiRepo.getAuthApi(new SecretData().getSecretData(mContext).getInt(SITE, 0));
            String req = new SecretData().req();
            api.getAuth(new AuthApi.ProfileAuth(login, pass, req, sha256(req + mSettings.getString(SALT, ""))))
                    .enqueue(new Callback<AuthApi.UserAuth>() {
                        @Override
                        public void onResponse(Call<AuthApi.UserAuth> call,
                                               retrofit2.Response<AuthApi.UserAuth> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                AuthApi.UserAuth user = response.body();
                                editor.putString(AUTH_TOKEN, user.getAuthToken()).apply();
                            } else {
                                mContext.startActivity(new Intent(mContext, AuthActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthApi.UserAuth> call, Throwable t) {
//overthink
                        }
                    });

        }
        return response;
    }
}