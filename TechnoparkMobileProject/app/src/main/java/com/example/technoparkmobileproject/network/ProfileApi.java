package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ProfileApi {

    @GET("profile/")
    Call<UserProfilePlain> getUserProfile(@Header("Authorization") String auth_token);

    @GET
    Call<UserProfilePlain> getOtherUserProfile(@Header("Authorization") String auth_token, @Url String url);

    class UserProfilePlain {



        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("project_id")
        @Expose
        private Integer projectId;
        @SerializedName("project")
        @Expose
        private String project;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("avatar_url")
        @Expose
        private String avatarUrl;
        @SerializedName("main_group")
        @Expose
        private String mainGroup;
        @SerializedName("birthdate")
        @Expose
        private String birthdate;
        @SerializedName("online")
        @Expose
        private Boolean online;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("subgroups")
        @Expose
        private List<Subgroup> subgroups = null;
        @SerializedName("activity")
        @Expose
        private Activity activity;
        @SerializedName("contacts")
        @Expose
        private List<Contact> contacts = null;
        @SerializedName("accounts")
        @Expose
        private List<Account> accounts = null;
        @SerializedName("rating")
        @Expose
        private Double rating;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getProjectId() {
            return projectId;
        }

        public void setProjectId(Integer projectId) {
            this.projectId = projectId;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getMainGroup() {
            return mainGroup;
        }

        public void setMainGroup(String mainGroup) {
            this.mainGroup = mainGroup;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public List<Subgroup> getSubgroups() {
            return subgroups;
        }

        public void setSubgroups(List<Subgroup> subgroups) {
            this.subgroups = subgroups;
        }

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public List<Contact> getContacts() {
            return contacts;
        }

        public void setContacts(List<Contact> contacts) {
            this.contacts = contacts;
        }

        public List<Account> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<Account> accounts) {
            this.accounts = accounts;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }


        public class Subgroup {

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

        public class Contact {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("value")
            @Expose
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

        public class Activity {

            @SerializedName("last_seen")
            @Expose
            private String lastSeen;
            @SerializedName("date_joined")
            @Expose
            private String dateJoined;

            public String getLastSeen() {
                return lastSeen;
            }

            public void setLastSeen(String lastSeen) {
                this.lastSeen = lastSeen;
            }

            public String getDateJoined() {
                return dateJoined;
            }

            public void setDateJoined(String dateJoined) {
                this.dateJoined = dateJoined;
            }

        }


        public class Account {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("value")
            @Expose
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

    }
}
