package com.example.technoparkmobileproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

import static com.example.technoparkmobileproject.TechnoparkApplication.AUTHORISATION;
import static com.example.technoparkmobileproject.TechnoparkApplication.NEWS_PATH_URL;

public interface NewsApi {

    @GET(NEWS_PATH_URL)
    Call<UserNewsPlain> getUserNews(@Header(AUTHORISATION) String auth_token);

    @GET
    Call<UserNewsPlain> getReUserNews(@Header(AUTHORISATION) String auth_token, @Url String url);

    class UserNewsPlain {

        public UserNewsPlain(Integer mCount, String mNext, String mPrevious, List<Result> mResult) {
            this.count = mCount;
            this.next = mNext;
            this.previous = mPrevious;
            this.results = mResult;
        }

        public UserNewsPlain() {

        }

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

        public void setNext(String next) {
            this.next = next;
        }

        public class TextShort {

            @SerializedName("content")
            @Expose
            public String content;
            @SerializedName("type")
            @Expose
            public String type;

            public TextShort(String mType, String mContent) {
                this.type = mType;
                this.content = mContent;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

        }

        public class Text {

            @SerializedName(value = "content", alternate = {"src"})
            @Expose
            public String content;

            @SerializedName("type")
            @Expose
            public String type;

            public Text(String mType, String mContent) {
                this.type = mType;
                this.content = mContent;
            }

            public Text() {

            }

            @SerializedName("lang")
            @Expose
            private String lang;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }


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


            public Result(Author mAuthor, String mBlog, String mTitle, Double mRating, String mDate, List<Text> mText,
                          int mCommentsCount, Integer mId, List<TextShort> mTextShort, String mUrl) {
                this.id = mId;
                this.blog = mBlog;
                this.author = mAuthor;
                this.commentsCount = mCommentsCount;
                this.publishDate = mDate;
                this.title = mTitle;
                this.rating = mRating;
                this.text = mText;
                this.textShort = mTextShort;
                this.url = mUrl;
                this.userVote = null;
                this.votesCount = null;
                this.forbidComment = null;
                this.favoritesCount = null;
            }

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

            public Author(String mFullname, String mAvatarUrl, String mUsername, int mId) {
                this.fullname = mFullname;
                this.avatarUrl = mAvatarUrl;
                this.username = mUsername;
                this.id = mId;
                this.rating = null;
                this.online = null;
            }
        }

    }

}

