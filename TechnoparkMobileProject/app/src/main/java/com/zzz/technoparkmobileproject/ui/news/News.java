package com.zzz.technoparkmobileproject.ui.news;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zzz.technoparkmobileproject.SecretData;

import java.util.ArrayList;
import java.util.List;

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
    public Boolean isOpen;

    public String next;

    Integer textSize;
    public int textShortSize;

    public News() {
    }

    public News(int key, String mTitle, String mBlog, String mAuthorName, Integer mAuthorId, String mAuthorUsername,
                String mAuthorAva, Integer mCommentsCount, String mPublishDate, Double mRating, List<UserNews.Text> mText,
                List<UserNews.TextShort> mTextShort, String mUrl, String mNext, Boolean mIsOpen) {
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

        isOpen = mIsOpen;
    }

    public List<UserNews.Text> getText() {
        List<String> listContent = new SecretData().parseStringToList(contentText);
        List<String> listType = new SecretData().parseStringToList(typeText);
        UserNews temp = new UserNews();
        List<UserNews.Text> text = new ArrayList<>();
        for (int i = 0; i < textSize; i++) {
            text.add(temp.new Text(listType.get(i), listContent.get(i)));
        }
        return text;
    }

    public List<UserNews.TextShort> getTextShort() {
        List<String> listContent = new SecretData().parseStringToList(contentShort);
        List<String> listType = new SecretData().parseStringToList(typeShort);
        UserNews temp = new UserNews();
        List<UserNews.TextShort> textShort = new ArrayList<>();
        for (int i = 0; i < textShortSize; i++) {
            textShort.add(temp.new TextShort(listType.get(i), listContent.get(i)));
        }
        return textShort;
    }

}
