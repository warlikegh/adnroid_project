package com.zzz.technoparkmobileproject.ui.shedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSchedule {

    public UserSchedule(Integer mId, String mDiscipline, String mTitle, String mShortTitle, String mSuperShortTitle,
                        String mDate, String mStartTime, String mEndTime, String mLocation, List<Group> mGroups,
                        Boolean checkingOpened, Boolean attended, String feedbackUrl) {
        this.id = mId;
        this.discipline = mDiscipline;
        this.title = mTitle;
        this.shortTitle = mShortTitle;
        this.superShortTitle = mSuperShortTitle;
        this.date = mDate;
        this.startTime = mStartTime;
        this.endTime = mEndTime;
        this.location = mLocation;
        this.groups = mGroups;
        this.checkingOpened = checkingOpened;
        this.attended = attended;
        this.feedbackUrl = feedbackUrl;
    }

    public UserSchedule() {
    }


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
    private Boolean checkingOpened;
    @SerializedName("attended")
    @Expose
    private Boolean attended;
    @SerializedName("feedback_url")
    @Expose
    private String feedbackUrl;

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

    public Boolean getCheckingOpened() {
        return checkingOpened;
    }

    public void setCheckingOpened(Boolean checkingOpened) {
        this.checkingOpened = checkingOpened;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }


    public class Group {

        public Group(Integer mId, String mName) {
            this.id = mId;
            this.name = mName;
        }

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

