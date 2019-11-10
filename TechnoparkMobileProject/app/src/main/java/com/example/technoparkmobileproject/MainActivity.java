package com.example.technoparkmobileproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.technoparkmobileproject.User.TechnoparkUser;
import com.example.technoparkmobileproject.User.UserCheck;
import com.example.technoparkmobileproject.User.UserNews;
import com.example.technoparkmobileproject.User.UserSchedule;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.technoparkmobileproject.AuthActivity.AUTH_TOKEN_EXTRA;

public class MainActivity extends AppCompatActivity{

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    public static final String AUTH_TOKEN_EXTRA = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /*  Here we can get data(ex. auth_token) and give it to fragments to make request for API
            Example of request

        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }try {
            mSettings = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor = mSettings.edit();
        String authToken = mSettings.getString(AUTH_TOKEN_EXTRA, "");

        NetworkService.getInstance()
                .getJSONApi()
                .getUserCheck(" Token "+authToken)
                .enqueue(new Callback<UserCheck>(){
                             @Override
                             public void onResponse(@NonNull Call<UserCheck> call, @NonNull Response<UserCheck> response) {
                                 if (response.isSuccessful()) {

                                 }
                                 else {

                                 }
                             }
                             @Override
                             public void onFailure(@NonNull Call<UserCheck> call, @NonNull Throwable t) {

                             }
                         }


        );
        */
    }




}
