package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface NewsApi {

    @GET("topics/subscribed/")
    Call<UserNewsPlain> getUserNews(@Header("Authorization") String auth_token);

    @GET
    Call<UserNewsPlain> getReUserNews(@Header("Authorization") String auth_token,@Url String url);

    class UserNewsPlain {

        @SerializedName("count")
        @Expose
        public Integer count;
        @SerializedName("next")
        @Expose
        public String next;
        @SerializedName("previous")
        @Expose
        public String previous;
        @SerializedName("results")
        @Expose
        public List<Result> results = null;


        public class TextShort {

            @SerializedName("content")
            @Expose
            public String content;
            @SerializedName("type")
            @Expose
            public String type;

        }

        public class Text {

            @SerializedName(value = "content", alternate = {"src"})
            @Expose
            public String content;

            @SerializedName("type")
            @Expose
            public String type;



        }
        public class Result {

            @SerializedName("id")
            @Expose
            public Integer id;
            @SerializedName("title")
            @Expose
            public String title;
            @SerializedName("blog")
            @Expose
            public String blog;
            @SerializedName("text")
            @Expose
            public List<Text> text = null;
            @SerializedName("text_short")
            @Expose
            public List<TextShort> textShort = null;
            @SerializedName("author")
            @Expose
            public Author author;
            @SerializedName("publish_date")
            @Expose
            public String publishDate;
            @SerializedName("forbid_comment")
            @Expose
            public Boolean forbidComment;
            @SerializedName("rating")
            @Expose
            public Double rating;
            @SerializedName("user_vote")
            @Expose
            public Object userVote;
            @SerializedName("votes_count")
            @Expose
            public Integer votesCount;
            @SerializedName("comments_count")
            @Expose
            public Integer commentsCount;
            @SerializedName("favorites_count")
            @Expose
            public Integer favoritesCount;
            @SerializedName("url")
            @Expose
            public String url;

        }

        public class Author {

            @SerializedName("id")
            @Expose
            public Integer id;
            @SerializedName("username")
            @Expose
            public String username;
            @SerializedName("fullname")
            @Expose
            public String fullname;
            @SerializedName("avatar_url")
            @Expose
            public String avatarUrl;
            @SerializedName("online")
            @Expose
            public Boolean online;
            @SerializedName("rating")
            @Expose
            public Double rating;
            }

        }

    }

