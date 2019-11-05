package com.example.technoparkmobileproject.User;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSchedule {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("discipline")
    @Expose
    private String discipline;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("short_title")
    @Expose
    private String shortTitle;
    @SerializedName("super_short_title")
    @Expose
    private String superShortTitle;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("groups")
    @Expose
    private List<Group> groups = null;
    @SerializedName("checkin_opened")
    @Expose
    private Boolean checkinOpened;
    @SerializedName("attended")
    @Expose
    private Boolean attended;
    @SerializedName("feedback_url")
    @Expose
    private Object feedbackUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getSuperShortTitle() {
        return superShortTitle;
    }

    public void setSuperShortTitle(String superShortTitle) {
        this.superShortTitle = superShortTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Boolean getCheckinOpened() {
        return checkinOpened;
    }

    public void setCheckinOpened(Boolean checkinOpened) {
        this.checkinOpened = checkinOpened;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public Object getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(Object feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }


    public class Group {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}