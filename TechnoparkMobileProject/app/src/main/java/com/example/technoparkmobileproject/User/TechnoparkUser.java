package com.example.technoparkmobileproject.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TechnoparkUser {

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
