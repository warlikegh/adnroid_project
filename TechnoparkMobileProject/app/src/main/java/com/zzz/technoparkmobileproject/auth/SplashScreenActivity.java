package com.zzz.technoparkmobileproject.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zzz.technoparkmobileproject.MainActivity;
import com.zzz.technoparkmobileproject.R;
import com.zzz.technoparkmobileproject.SecretData;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_AUTHORISED;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences mSettings;

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
