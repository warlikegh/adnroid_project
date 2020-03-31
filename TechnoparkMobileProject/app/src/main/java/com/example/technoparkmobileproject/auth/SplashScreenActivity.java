package com.example.technoparkmobileproject.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.technoparkmobileproject.MainActivity;
import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.SecretData;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.example.technoparkmobileproject.auth.AuthRepo.IS_AUTHORISED;
import static com.example.technoparkmobileproject.auth.AuthRepo.LOGIN;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences mSettings;
    static String AUTH_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mSettings = new SecretData().getSecretData(this);

        if (mSettings.getBoolean(IS_AUTHORISED, false)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            startActivity(new Intent(getApplicationContext(), AuthActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
