package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PushApi {

    @POST("registerAPN/")
    Call<PushSuccess> registerAPN(@Header("Authorization") String auth_token, @Body UserPush data);

    class PushSuccess {

        @SerializedName("message")
        @Expose
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    class UserPush {

        public UserPush() {

        }

        public UserPush(String deviceId, String deviceToken) {
            this.deviceId = deviceId;
            this.deviceToken = deviceToken;
        }

        @SerializedName("device_id")
        @Expose
        private String deviceId;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

    }
}
