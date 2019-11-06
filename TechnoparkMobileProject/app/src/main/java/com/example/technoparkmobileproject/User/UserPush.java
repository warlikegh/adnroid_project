package com.example.technoparkmobileproject.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPush {

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