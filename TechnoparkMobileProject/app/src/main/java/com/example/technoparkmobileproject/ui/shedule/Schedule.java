package com.example.technoparkmobileproject.ui.shedule;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.technoparkmobileproject.SecretData;

import java.util.ArrayList;
import java.util.List;

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
                    String mStartTime, String mEndTime, List<UserSchedule.Group> mGroups, String mLocation) {
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

    public List<UserSchedule.Group> getGroup() {
        List<String> listName = new SecretData().parseStringToList(groups);
        List<String> listId = new SecretData().parseStringToList(groupid);
        UserSchedule temp = new UserSchedule();
        List<UserSchedule.Group> groups = new ArrayList<>();
        for (int i = 0; i < textSize; i++) {
            groups.add(temp.new Group(Integer.parseInt(listId.get(i)), listName.get(i)));
        }
        return groups;
    }

}