package com.example.technoparkmobileproject;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SecretData {
    static SharedPreferences mSettings;
    String DIVIDER = "AHoP8";

    public SharedPreferences getSecretData(Context context) {
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mSettings = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mSettings;
    }

    public String req() {
        return new BigInteger(16 * 4, new Random()).toString(16);
    }

    public String parseListToString(List<String> list, int textSize) {
        String string = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                string = string.concat(list.get(i));
            } else {
                string = string.concat(" ");
            }
            string = string.concat(DIVIDER);
        }
        return string;
    }

    public List<String> parseStringToList(String string) {
        List<String> list = new ArrayList<>();
        Integer index = string.indexOf(DIVIDER);
        while (index > 0) {
            list.add(string.substring(0, index));
            string = string.substring(index + DIVIDER.length());
            index = string.indexOf(DIVIDER);
        }
        return list;
    }

    public Date getDate(String stringDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = df.parse(stringDate.replace("T", " ").replace("Z", ""));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getDateString(String stringDate) {
        Date date = getDate(stringDate);
        String day = "";
        if (date.getDate() < 10) {
            day = "0";
        }
        day = day + ((Integer) date.getDate()).toString();
        String month = "";
        if ((date.getMonth() + 1) < 10) {
            month = "0";
        }
        month = month + ((Integer) (date.getMonth() + 1)).toString();
        String year = ((Integer) (date.getYear() + 1900)).toString();
        return day + "." + month + "." + year;
    }

    public String getTimeString(String stringDate) {
        Date date = getDate(stringDate);

        String hour = "";
        if (date.getHours() < 10) {
            hour = "0";
        }
        hour = hour + ((Integer) date.getHours()).toString();

        String minute = "";
        if (date.getMinutes() < 10) {
            minute = "0";
        }
        minute = minute + ((Integer) date.getMinutes()).toString();
        return hour + ":" + minute;
    }


}
