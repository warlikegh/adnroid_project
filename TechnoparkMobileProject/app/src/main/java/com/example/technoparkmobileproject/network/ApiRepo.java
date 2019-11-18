package com.example.technoparkmobileproject.network;

import android.content.Context;

import com.example.technoparkmobileproject.TechnoparkApplication;
import com.example.technoparkmobileproject.auth.AuthRepo;
import com.example.technoparkmobileproject.auth.AuthViewModel;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
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

    public ApiRepo() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
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

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Request.Builder builder = request.newBuilder();
           // builder.header("Accept", "application/json"); //if necessary, say to consume JSON

          //  String token = settings.getAccessToken(); //save token of this request for future
         //   setAuthHeader(builder, token); //write current token to request

            request = builder.build(); //overwrite old request
            Response response = chain.proceed(request); //perform request, here original request will be executed

            if (response.code() == 401) { //if unauthorized
                              /*  synchronized (mOkHttpClient) { //perform all 401 in sync blocks, to avoid multiply token updates
                    String currentToken = settings.getAccessToken(); //get currently stored token

                    if(currentToken != null && currentToken.equals(token)) { //compare current token with token that was stored before, if it was not updated - do update

                        int code = refreshToken() / 100; //refresh token
                        if(code != 2) { //if refresh token failed for some reason
                            if(code == 4) //only if response is 400, 500 might mean that token was not updated
                                logout(); //go to login screen
                            return response; //if token refresh failed - show error to user
                        }
                    }

                    if(settings.getAccessToken() != null) { //retry requires new auth token,
                        setAuthHeader(builder, settings.getAccessToken()); //set auth token to updated
                        request = builder.build();
                        return chain.proceed(request); //repeat request with new token
                    }
                }*/


               // AuthViewModel authViewModel=new AuthViewModel(TechnoparkApplication.from());


              /*  final ApiRepo mApiRepo;
                AuthApi api = mApiRepo.getAuthApi();
                String req=new BigInteger(16 * 4, new Random()).toString(16);
                api.getAuth(new AuthApi.ProfileAuth(login,password,req, sha256(req+mSettings.getString(SALT,""))))
                        .enqueue(new Callback<AuthApi.UserAuth>() {
                            @Override
                            public void onResponse(Call<AuthApi.UserAuth> call,
                                                   retrofit2.Response<AuthApi.UserAuth> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    AuthApi.UserAuth user = response.body();
                                    editor.putString(AUTH_TOKEN, user.getAuthToken()).apply();
                                    editor.putString(LOGIN, login).apply();
                                    editor.putString(PASSWORD, password).apply();

                                    progress.postValue(AuthRepo.AuthProgress.SUCCESS);
                                } else {
                                    progress.postValue(AuthRepo.AuthProgress.FAILED);
                                }
                            }

                            @Override
                            public void onFailure(Call<AuthApi.UserAuth> call, Throwable t) {
                                progress.postValue(AuthRepo.AuthProgress.FAILED_NET);
                            }
                        });*/

            }

            return response;
        }

 /*       private void setAuthHeader(Request.Builder builder, String token) {
            if (token != null) //Add Auth token to each request if authorized
                builder.header("Authorization", String.format("Bearer %s", token));
        }*/


     /*   private int refreshToken() {
            //Refresh token, synchronously, save it, and return result code
            //you might use retrofit here
        }

        private int logout() {
            //logout your user
        }*/
    }
}

