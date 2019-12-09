package com.example.technoparkmobileproject.ui.news;

import android.util.Log;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    String DIVIDER = "AHoP8";


    public News() {
    }

    public News(int key, String mTitle, String mBlog, String mAuthorName, Integer mAuthorId, String mAuthorAva,
                Integer mCommentsCount, String mPublishDate, Double mRating, List<NewsApi.UserNewsPlain.Text> mText,
                List<NewsApi.UserNewsPlain.TextShort> mTextShort, String mUrl, String mNext) {
        id = key;
        title = mTitle;
        blog = mBlog;
        authorName = mAuthorName;
        authorId = mAuthorId;
        authorAva = mAuthorAva;
        commentsCount = mCommentsCount;
        publishDate = mPublishDate;
        rating = mRating;

        List<String> tempContent = new ArrayList<>();
        List<String> tempType = new ArrayList<>();
        for (int j = 0; j < mText.size(); j++) {
            tempContent.add(mText.get(j).getContent());
            tempType.add(mText.get(j).getType());
        }
        contentText = parseListToString(tempContent, mText.size());
        typeText = parseListToString(tempType, mText.size());

        tempContent.clear();
        tempType.clear();
        for (int j = 0; j < mTextShort.size(); j++) {
            tempContent.add(mTextShort.get(j).getContent());
            tempType.add(mTextShort.get(j).getType());
        }
        contentShort = parseListToString(tempContent, mTextShort.size());
        typeShort = parseListToString(tempType, mTextShort.size());
        url = mUrl;
        next = mNext;

        textSize = mText.size();
        textShortSize = mTextShort.size();
    }

    public List<NewsApi.UserNewsPlain.Text> getText() {
        List<String> listContent = parseStringToList(contentText, textSize);
        List<String> listType = parseStringToList(typeText, textSize);
        NewsApi.UserNewsPlain temp = new NewsApi.UserNewsPlain();
        List<NewsApi.UserNewsPlain.Text> text = new ArrayList<>();
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

    public List<NewsApi.UserNewsPlain.TextShort> getTextShort() {
        List<String> listContent = parseStringToList(contentShort, textShortSize);
        List<String> listType = parseStringToList(typeShort, textShortSize);
        NewsApi.UserNewsPlain temp = new NewsApi.UserNewsPlain();
        List<NewsApi.UserNewsPlain.TextShort> textShort = new ArrayList<>();
        for (int i = 0; i < textShortSize; i++) {
            textShort.add(temp.new TextShort(listType.get(i), listContent.get(i)));
        }
        return textShort;
    }

    private String parseListToString(List<String> list, int textSize) {
        String string = "";
        for (int i = 0; i < textSize; i++) {
            if (list.get(i) != null) {
                string = string.concat(list.get(i));
            }
            else {
                string = string.concat(" ");
            }
                string = string.concat(DIVIDER);

        }
        return string;
    }

    private List<String> parseStringToList(String string, int textSize) {
        List<String> list = new ArrayList<>();
        Integer index = string.indexOf(DIVIDER);
        ;
        //int j = 0;

        while ((index > 0)) {
            list.add(string.substring(0, index));
            string = string.substring(index + DIVIDER.length());
            index = string.indexOf(DIVIDER);
        }
        return list;
    }

}
