package com.example.technoparkmobileproject.ui.news;

import android.util.Log;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.network.NewsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "News")
public class News {

    @PrimaryKey
    public int id;

    public String title;
    public String blog;
    public String authorName;
    public Integer authorId;
    public String authorAva;
    public String authorUsername;
    public Integer commentsCount;
    public String publishDate;
    public Double rating;
    public String contentText;
    public String typeText;
    public String contentShort;
    public String typeShort;
    public String url;

    public String next;

    Integer textSize;
    public int textShortSize;

    public News() {
    }

    public News(int key, String mTitle, String mBlog, String mAuthorName, Integer mAuthorId, String mAuthorUsername,
                String mAuthorAva, Integer mCommentsCount, String mPublishDate, Double mRating, List<UserNews.Text> mText,
                List<UserNews.TextShort> mTextShort, String mUrl, String mNext) {
        id = key;
        title = mTitle;
        blog = mBlog;
        authorName = mAuthorName;
        authorId = mAuthorId;
        authorAva = mAuthorAva;
        authorUsername = mAuthorUsername;
        commentsCount = mCommentsCount;
        publishDate = mPublishDate;
        rating = mRating;

        List<String> tempContent = new ArrayList<>();
        List<String> tempType = new ArrayList<>();
        for (int j = 0; j < mText.size(); j++) {
            tempContent.add(mText.get(j).getContent());
            tempType.add(mText.get(j).getType());
        }
        contentText = new SecretData().parseListToString(tempContent, mText.size());
        typeText = new SecretData().parseListToString(tempType, mText.size());

        tempContent.clear();
        tempType.clear();
        for (int j = 0; j < mTextShort.size(); j++) {
            tempContent.add(mTextShort.get(j).getContent());
            tempType.add(mTextShort.get(j).getType());
        }
        contentShort = new SecretData().parseListToString(tempContent, mTextShort.size());
        typeShort = new SecretData().parseListToString(tempType, mTextShort.size());
        url = mUrl;
        next = mNext;

        textSize = mText.size();
        textShortSize = mTextShort.size();
    }

    /*qu*/
    public List<UserNews.Text> getText() {
        List<String> listContent = new SecretData().parseStringToList(contentText);
        List<String> listType = new SecretData().parseStringToList(typeText);
        /*qu*/
        UserNews temp = new UserNews();
        /*qu*/
        List<UserNews.Text> text = new ArrayList<>();
        /*Log.d("database", textSize.toString());
        Log.d("database", url);
        Log.d("database", title);
        for (int k = 0; k < listContent.size(); k++) {
            Log.d("database", listContent.get(k));
        }
        for (int k = 0; k < listType.size(); k++) {
            Log.d("database", listType.get(k));
        }*/
        for (int i = 0; i < textSize; i++) {
            text.add(temp.new Text(listType.get(i), listContent.get(i)));
        }
        return text;
    }

    /*qu*/
    public List<UserNews.TextShort> getTextShort() {
        List<String> listContent = new SecretData().parseStringToList(contentShort);
        List<String> listType = new SecretData().parseStringToList(typeShort);
        /*qu*/
        UserNews temp = new UserNews();
        /*qu*/
        List<UserNews.TextShort> textShort = new ArrayList<>();
        for (int i = 0; i < textShortSize; i++) {
            textShort.add(temp.new TextShort(listType.get(i), listContent.get(i)));
        }
        return textShort;
    }

}
