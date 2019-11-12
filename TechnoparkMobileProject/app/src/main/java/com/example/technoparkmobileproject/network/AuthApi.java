package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface AuthApi {

    @POST("auth/")
    Call<UserAuth> getAuth(@Body ProfileAuth data);

    class ProfileAuth {

        @SerializedName("req_id")
        @Expose
        private String reqId;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("login")
        @Expose
        private String login;
        @SerializedName("password")
        @Expose
        private String password;

        public ProfileAuth(String mLogin, String mPassword, String mReq, String mToken){
            this.login = mLogin;
            this.password = mPassword;
            this.reqId = mReq;
            this.token = mToken;
        }

        public String getReqId() {
            return reqId;
        }

        public void setReqId(String reqId) {
            this.reqId = reqId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }


    class UserAuth {

        @SerializedName("auth_token")
        @Expose
        private String authToken;

        @SerializedName("user_id")
        @Expose
        private String userId;

        @SerializedName("username")
        @Expose
        private String username;

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }



    }
}