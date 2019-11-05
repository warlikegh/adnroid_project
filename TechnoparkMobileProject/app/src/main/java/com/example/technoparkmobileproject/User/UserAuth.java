package com.example.technoparkmobileproject.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuth {

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
