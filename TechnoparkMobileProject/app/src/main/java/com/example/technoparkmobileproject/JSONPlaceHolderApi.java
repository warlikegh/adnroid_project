package com.example.technoparkmobileproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JSONPlaceHolderApi {
    @POST("auth/")
    public Call<TechnoparkUser> getPostData(@Body UserAuth data);
}
