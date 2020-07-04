package com.zzz.technoparkmobileproject.group;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Groups")
public class Group {

    @PrimaryKey
    public int id;

    public int idGroup;
    public String nameGroup;

    public Integer idUser;
    public String username;
    public String fullname;
    public String avatarUrl;
    public Boolean online;
    public Double rating;


    public Group(Integer id, Integer idGroup, String nameGroup, Integer idUser, String username,
                 String fullname, String avatarUrl, Boolean online, Double rating) {
        this.id = id;
        this.idGroup = idGroup;
        this.nameGroup = nameGroup;
        this.idUser = idUser;
        this.username = username;
        this.fullname = fullname;
        this.avatarUrl = avatarUrl;
        this.online = online;
        this.rating = rating;
    }

}
