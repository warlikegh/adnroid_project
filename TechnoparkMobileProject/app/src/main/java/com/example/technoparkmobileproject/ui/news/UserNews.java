package com.example.technoparkmobileproject.ui.news;

import com.example.technoparkmobileproject.network.NewsApi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class UserNews {

    public UserNews() {
        this.count = null;
        this.next = null;
        this.previous = null;
        this.results = null;
    }

    public UserNews(Integer mCount, String mNext, String mPrevious, List<Result> mResult) {
        this.count = mCount;
        this.next = mNext;
        this.previous = mPrevious;
        this.results = mResult;
    }


    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public class TextShort {

        @SerializedName(value = "content", alternate = "src")
        @Expose
        private String content;
        @SerializedName("type")
        @Expose
        private String type;

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

        public Text(String mType, String mContent) {
            this.type = mType;
            this.content = mContent;
            this.lang=null;
        }

        @SerializedName(value = "content", alternate = "src")
        @Expose
        private String content;

        @SerializedName("type")
        @Expose
        private String type;

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

        public void setType(String lang) {
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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("blog")
        @Expose
        private String blog;
        @SerializedName("text")
        @Expose
        private List<Text> text = null;
        @SerializedName("text_short")
        @Expose
        private List<TextShort> textShort = null;
        @SerializedName("author")
        @Expose
        private Author author;
        @SerializedName("publish_date")
        @Expose
        private String publishDate;
        @SerializedName("forbid_comment")
        @Expose
        private Boolean forbidComment;
        @SerializedName("rating")
        @Expose
        private Double rating;
        @SerializedName("user_vote")
        @Expose
        private Object userVote;
        @SerializedName("votes_count")
        @Expose
        private Integer votesCount;
        @SerializedName("comments_count")
        @Expose
        private Integer commentsCount;
        @SerializedName("favorites_count")
        @Expose
        private Integer favoritesCount;
        @SerializedName("url")
        @Expose
        private String url;

        public Result() {

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBlog() {
            return blog;
        }

        public void setBlog(String blog) {
            this.blog = blog;
        }

        public List<Text> getText() {
            return text;
        }

        public void setText(List<Text> text) {
            this.text = text;
        }

        public List<TextShort> getTextShort() {
            return textShort;
        }

        public void setTextShort(List<TextShort> textShort) {
            this.textShort = textShort;
        }

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public Boolean getForbidComment() {
            return forbidComment;
        }

        public void setForbidComment(Boolean forbidComment) {
            this.forbidComment = forbidComment;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public Object getUserVote() {
            return userVote;
        }

        public void setUserVote(Object userVote) {
            this.userVote = userVote;
        }

        public Integer getVotesCount() {
            return votesCount;
        }

        public void setVotesCount(Integer votesCount) {
            this.votesCount = votesCount;
        }

        public Integer getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(Integer commentsCount) {
            this.commentsCount = commentsCount;
        }

        public Integer getFavoritesCount() {
            return favoritesCount;
        }

        public void setFavoritesCount(Integer favoritesCount) {
            this.favoritesCount = favoritesCount;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    public class Author {

        public Author(String mFullname, String mAvatarUrl, String mUsername, int mId) {
            this.fullname = mFullname;
            this.avatarUrl = mAvatarUrl;
            this.username = mUsername;
            this.id = mId;
            this.rating = null;
            this.online = null;
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