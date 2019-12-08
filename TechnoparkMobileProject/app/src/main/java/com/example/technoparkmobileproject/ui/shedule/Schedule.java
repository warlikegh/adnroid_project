package com.example.technoparkmobileproject.ui.shedule;


import android.util.Log;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    String DIVIDER = "AHoP8";


    public Schedule() {
    }

    public Schedule(int key, String mTitle, String mDiscipline, String mShortTitle, String mSuperShortTitle, String mDate,
                 String mStartTime, String  mEndTime, List<ScheduleApi.UserSchedulePlain.Group> mGroups, String mLocation) {
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
        groups = parseListToString(tempName, mGroups.size());
        groupid = parseListToString(tempId, mGroups.size());

        textSize = mGroups.size();

    }

    public List<ScheduleApi.UserSchedulePlain.Group> getGroup() {
        List<String> listName = parseStringToList(groups, textSize);
        List<String> listId = parseStringToList(groupid, textSize);
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