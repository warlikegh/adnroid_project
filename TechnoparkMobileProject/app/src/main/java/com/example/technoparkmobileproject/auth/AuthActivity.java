package com.example.technoparkmobileproject.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.technoparkmobileproject.MainActivity;
import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.SecretData;

import static com.example.technoparkmobileproject.TechnoparkApplication.IS_AUTHORISED;


public class AuthActivity extends FragmentActivity implements Router {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_main);

        SharedPreferences mSettings= new SecretData().getSecretData(this);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_AUTHORISED, false).apply();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AuthFragment())
                    .commit();
        }
    }

    @Override
    public void openMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}

