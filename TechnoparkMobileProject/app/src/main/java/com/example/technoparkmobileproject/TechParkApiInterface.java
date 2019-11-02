package com.example.technoparkmobileproject;

import retrofit.http.GET;
import retrofit2.Call;
import retrofit2.http.Query;

public interface TechParkApiInterface {
    @GET("auth/")
    Call<TechnoparkUser> getUser(@Query("req_id") String req_id,
                                 @Query("token") String token,
                                 @Query("login") String login,
                                 @Query("password") String password);



}
