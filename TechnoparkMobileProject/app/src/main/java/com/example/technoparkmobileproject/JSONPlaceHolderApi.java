package com.example.technoparkmobileproject;

import com.example.technoparkmobileproject.User.PushSuccess;
import com.example.technoparkmobileproject.User.TechnoparkUser;
import com.example.technoparkmobileproject.User.UserAuth;
import com.example.technoparkmobileproject.User.UserCheck;
import com.example.technoparkmobileproject.User.UserNews;
import com.example.technoparkmobileproject.User.UserProfile;
import com.example.technoparkmobileproject.User.UserPush;
import com.example.technoparkmobileproject.User.UserSchedule;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JSONPlaceHolderApi {
    @POST("auth/")
    Call<TechnoparkUser> getPostData(@Body UserAuth data);

    @GET("profile/")
    Call<UserProfile> getUserProfile(@Header("Authorization") String auth_token);

    @GET("topics/subscribed/?limit=100&offset=15")
    Call<UserNews> getUserNews(@Header("Authorization") String auth_token);

    @GET("schedule/9101/check/")
    Call<UserCheck> check(@Header("Authorization") String auth_token);

    @POST("registerAPN/")
    Call<PushSuccess> registerAPN(@Header("Authorization") String auth_token, @Body UserPush data);

    @GET("schedule/9101/check/")
    Call<UserSchedule> getUserSchedule(@Header("Authorization") String auth_token);

}
