package com.example.technoparkmobileproject.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technoparkmobileproject.MainActivity;
import com.example.technoparkmobileproject.R;



public class AuthActivity extends AppCompatActivity implements Router {

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    public static final String AUTH_TOKEN_EXTRA = "auth_token";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AuthTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_main);
        //проверка
        //если аутентифицироваться надо, то
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AuthFragment(), "Login")
                    .commit();
        }
        //если нет, то openMain();



        /*  We`re planning to realize EncryptedSharedPreferences for save secret data such as
        salt, login, password, auth_token (maybe device_id, device_token, user_id, username)

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
        editor.putString(AUTH_TOKEN_EXTRA, post.getAuthToken());                 // such we planning to save data
                                    editor.apply();
        */

    }
    @Override
    public void openMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}

