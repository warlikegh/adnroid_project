package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PushApi {

    @POST("device_token/")
    Call<PushSuccess> registerAPN(@Header("Authorization") String auth_token, @Body UserPush data);

    @HTTP(method = "DELETE", path = "device_token/", hasBody = true)
    Call<PushSuccess> deleteToken(@Header("Authorization") String auth_token, @Body UserToken data);

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
            this.type = "android";
        }

        @SerializedName("device_id")
        @Expose
        private String deviceId;
        @SerializedName("token")
        @Expose
        private String deviceToken;
        @SerializedName("platform")
        @Expose
        private String type;

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

    class UserToken {

        public UserToken() {

        }

        public UserToken(String deviceToken) {
            this.deviceToken = deviceToken;
            this.type = "android";
        }

        @SerializedName("token")
        @Expose
        private String deviceToken;
        @SerializedName("platform")
        @Expose
        private String type;

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

    }
}
