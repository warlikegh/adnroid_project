package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface CheckApi {
    @GET("schedule/")/*9101/check/*/
    Call<UserCheck> checkUser(@Header("Authorization") String auth_token, @Url String url);

    class UserCheck {
        @SerializedName("schedule_item")
        @Expose
        private Integer scheduleItem;
        @SerializedName("feedback_url")
        @Expose
        private String feedbackUrl;

        public Integer getScheduleItem() {
            return scheduleItem;
        }

        public void setScheduleItem(Integer scheduleItem) {
            this.scheduleItem = scheduleItem;
        }

        public String getFeedbackUrl() {
            return feedbackUrl;
        }

        public void setFeedbackUrl(String feedbackUrl) {
            this.feedbackUrl = feedbackUrl;
        }

    }
}
