package com.example.technoparkmobileproject.ui.shedule;


import android.util.Log;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.network.ScheduleApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "Schedule")
public class Schedule {

    @PrimaryKey
    public int id;

    public String title;
    public String discipline;
    public String shortTitle;
    public String superShortTitle;
    public String date;
    public String startTime;
    public String endTime;
    public String location;
    String groups;
    String groupid;


    Integer textSize;
    public int textShortSize;


    public Schedule() {
    }

    public Schedule(int key, String mTitle, String mDiscipline, String mShortTitle, String mSuperShortTitle, String mDate,
                    String mStartTime, String mEndTime, List<ScheduleApi.UserSchedulePlain.Group> mGroups, String mLocation) {
        this.id = key;
        this.discipline = mDiscipline;
        this.title = mTitle;
        this.shortTitle = mShortTitle;
        this.superShortTitle = mSuperShortTitle;
        this.date = mDate;
        this.startTime = mStartTime;
        this.endTime = mEndTime;
        this.location = mLocation;


        List<String> tempName = new ArrayList<>();
        List<String> tempId = new ArrayList<>();
        for (int j = 0; j < mGroups.size(); j++) {
            tempName.add(mGroups.get(j).getName());
            tempId.add(mGroups.get(j).getId().toString());
        }
        groups = new SecretData().parseListToString(tempName, mGroups.size());
        groupid = new SecretData().parseListToString(tempId, mGroups.size());

        textSize = mGroups.size();

    }

    public List<ScheduleApi.UserSchedulePlain.Group> getGroup() {
        List<String> listName = new SecretData().parseStringToList(groups);
        List<String> listId = new SecretData().parseStringToList(groupid);
        ScheduleApi.UserSchedulePlain temp = new ScheduleApi.UserSchedulePlain();
        List<ScheduleApi.UserSchedulePlain.Group> groups = new ArrayList<>();
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
            groups.add(temp.new Group(Integer.parseInt(listId.get(i)), listName.get(i)));
        }
        return groups;
    }

}