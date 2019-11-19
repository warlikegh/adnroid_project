package com.example.technoparkmobileproject.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technoparkmobileproject.MainActivity;
import com.example.technoparkmobileproject.R;


public class AuthActivity extends AppCompatActivity implements Router {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
            setTheme(R.style.AuthTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.auth_main);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AuthFragment(), "Login")
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

