package com.example.technoparkmobileproject.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.technoparkmobileproject.MainActivity;
import com.example.technoparkmobileproject.R;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class AuthActivity extends AppCompatActivity implements Router {

    Button enter;
    TextView result;
    EditText mLogin;
    EditText mPassword;
    ProgressBar mProgressBar;
    ViewPager mViewPager;

    private AuthViewModel mAuthViewModel;

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

