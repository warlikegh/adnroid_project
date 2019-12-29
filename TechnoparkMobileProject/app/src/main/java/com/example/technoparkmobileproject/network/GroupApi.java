package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface GroupApi {


    @GET
    Call<StudentGroupPlain> getStudentsGroup(@Header("Authorization") String auth_token, @Url String url);

    class StudentGroupPlain {

        public StudentGroupPlain() {
        }

        public StudentGroupPlain(Integer id, String name, List<Student> students) {
            this.id = id;
            this.name = name;
            this.students = students;
        }

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("students")
        @Expose
        private List<Student> students = null;

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

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        public class Student {

            public Student() {
            }

            public Student(Integer id, String username, String fullname, String avatarUrl, Boolean online, Double rating) {
                this.id = id;
                this.username = username;
                this.fullname = fullname;
                this.avatarUrl = avatarUrl;
                this.online = online;
                this.rating = rating;
            }

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("fullname")
            @Expose
            private String fullname;
            @SerializedName("avatar_url")
            @Expose
            private String avatarUrl;
            @SerializedName("online")
            @Expose
            private Boolean online;
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

            public String getFullname() {
                return fullname;
            }

            public void setFullname(String fullname) {
                this.fullname = fullname;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public Boolean getOnline() {
                return online;
            }

            public void setOnline(Boolean online) {
                this.online = online;
            }

            public Double getRating() {
                return rating;
            }

            public void setRating(Double rating) {
                this.rating = rating;
            }

        }

    }
}
