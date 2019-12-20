package com.example.technoparkmobileproject;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
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
            }
            else {
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

}
