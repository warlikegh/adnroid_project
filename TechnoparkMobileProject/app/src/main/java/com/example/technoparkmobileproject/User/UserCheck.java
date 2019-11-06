package com.example.technoparkmobileproject.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCheck {

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