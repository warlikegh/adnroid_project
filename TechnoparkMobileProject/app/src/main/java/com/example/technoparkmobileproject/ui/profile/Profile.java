package com.example.technoparkmobileproject.ui.profile;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.technoparkmobileproject.SecretData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "Profile")
public class Profile {
    @PrimaryKey
    public int id;

    public String username;
    public Integer projectId;
    public String project;
    public String fullname;
    public String gender;
    public String avatarUrl;
    public String mainGroup;
    public String birthdate;
    public Boolean online;
    public String about;
    String subgroupsId;
    String subgroupsName;
    int subgroupsSize;
    String lastSeen;
    String dateJoined;
    String contactsName;
    String contactsValue;
    int contactsSize;
    String accountsName;
    String accountsValue;
    int accountsSize;
    public Double rating;

    public Profile() {
    }

    public Profile(int id, String username, Integer projectId, String project, String fullname,
                   String gender, String avatarUrl, String mainGroup, String birthdate, Boolean online, String about,
                   List<UserProfile.Subgroup> subgroups,
                   UserProfile.Activity activity,
                   List<UserProfile.Contact> contacts,
                   List<UserProfile.Account> accounts,
                   Double rating) {
        this.id = id;
        this.username = username;
        this.projectId = projectId;
        this.project = project;
        this.fullname = fullname;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.mainGroup = mainGroup;
        this.birthdate = birthdate;
        this.online = online;
        this.about = about;

        this.subgroupsSize = subgroups.size();
        List<String> tempSubgroupsId = new ArrayList<>();
        List<String> tempSubgroupsName = new ArrayList<>();
        for (int j = 0; j < subgroupsSize; j++) {
            tempSubgroupsId.add(subgroups.get(j).getId().toString());
            tempSubgroupsName.add(subgroups.get(j).getName());
        }
        this.subgroupsId = new SecretData().parseListToString(tempSubgroupsId, subgroupsSize);
        this.subgroupsName = new SecretData().parseListToString(tempSubgroupsName, subgroupsSize);


        this.lastSeen = activity.getLastSeen();
        this.dateJoined = activity.getDateJoined();

        this.contactsSize = contacts.size();
        List<String> tempContactsName = new ArrayList<>();
        List<String> tempContactsValue = new ArrayList<>();
        for (int j = 0; j < contactsSize; j++) {
            tempContactsName.add(contacts.get(j).getName());
            if (contacts.get(j).getValue() != null && !Objects.equals(contacts.get(j).getValue(), ""))
                tempContactsValue.add(contacts.get(j).getValue());
            else
                tempContactsValue.add(" ");
        }
        this.contactsName = new SecretData().parseListToString(tempContactsName, contactsSize);
        this.contactsValue = new SecretData().parseListToString(tempContactsValue, contactsSize);
        this.accountsSize = accounts.size();
        List<String> tempAccountsName = new ArrayList<>();
        List<String> tempAccountsValue = new ArrayList<>();
        for (int j = 0; j < accountsSize; j++) {
            tempAccountsName.add(accounts.get(j).getName());
            tempAccountsValue.add(accounts.get(j).getValue());
        }
        this.accountsName = new SecretData().parseListToString(tempAccountsName, accountsSize);
        this.accountsValue = new SecretData().parseListToString(tempAccountsValue, accountsSize);

        this.rating = rating;
    }

    public List<UserProfile.Subgroup> getSubgroups() {
        List<String> listId = new SecretData().parseStringToList(subgroupsId);
        List<String> listName = new SecretData().parseStringToList(subgroupsName);
        UserProfile temp = new UserProfile();
        List<UserProfile.Subgroup> subgroups = new ArrayList<>();
        for (int i = 0; i < subgroupsSize; i++) {
            subgroups.add(temp.new Subgroup(Integer.parseInt(listId.get(i)), listName.get(i)));
        }
        return subgroups;
    }

    public UserProfile.Activity getActivity() {
        UserProfile temp = new UserProfile();
        return temp.new Activity(lastSeen, dateJoined);
    }

    public List<UserProfile.Contact> getContacts() {
        List<UserProfile.Contact> contacts = new ArrayList<>();
        List<String> listName = new SecretData().parseStringToList(contactsName);
        List<String> listValue = new SecretData().parseStringToList(contactsValue);
        UserProfile temp = new UserProfile();

        for (int i = 0; i < contactsSize; i++) {
            if (listValue.size() > 1)
                contacts.add(temp.new Contact(listName.get(i), listValue.get(i)));
            else
                contacts.add(temp.new Contact(listName.get(i), "-"));
        }
        return contacts;

    }

    public List<UserProfile.Account> getAccounts() {
        List<String> listName = new SecretData().parseStringToList(accountsName);
        List<String> listValue = new SecretData().parseStringToList(accountsValue);
        UserProfile temp = new UserProfile();
        List<UserProfile.Account> accounts = new ArrayList<>();
        for (int i = 0; i < accountsSize; i++) {
            accounts.add(temp.new Account(listName.get(i), listValue.get(i)));
        }
        return accounts;
    }


}
