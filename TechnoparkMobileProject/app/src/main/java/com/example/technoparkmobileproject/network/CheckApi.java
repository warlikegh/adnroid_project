package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

import static com.example.technoparkmobileproject.TechnoparkApplication.AUTHORISATION;

public interface CheckApi {
    @POST
    Call<UserCheck> checkUser(@Header(AUTHORISATION) String auth_token, @Url String url, @Body Nothing nothing);

    class Nothing {
        public Nothing() {

        }
    }

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
