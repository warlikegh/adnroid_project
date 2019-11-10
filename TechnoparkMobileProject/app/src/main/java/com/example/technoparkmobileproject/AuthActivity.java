package com.example.technoparkmobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.technoparkmobileproject.User.TechnoparkUser;
import com.example.technoparkmobileproject.User.UserAuth;


import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    Button enter;
    TextView result;
    UserAuth mData;
    EditText mLogin;
    EditText mPassword;

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    public static final String AUTH_TOKEN_EXTRA = "auth_token";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTheme(R.style.AuthTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_main);

        result = findViewById(R.id.result);
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);
        enter = findViewById(R.id.getBtn);

        mData = new UserAuth();

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
        */

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mLogin.getText().toString();
                String pass = mPassword.getText().toString();
                String req = new BigInteger(16 * 4, new Random()).toString(16);   // generate random hexadecimal string length 16 symbols
                String salt = "";                                                               // here salt and nothing will be work without it

                mData.setLogin(login);
                mData.setPassword(pass);
                mData.setReqId(req);
                mData.setToken(sha256(req+salt));

                NetworkService.getInstance()
                        .getJSONApi()
                        .getPostData(mData)
                        .enqueue(new Callback<TechnoparkUser>() {
                            @Override
                            public void onResponse(@NonNull Call<TechnoparkUser> call, @NonNull Response<TechnoparkUser> response) {
                                if (response.isSuccessful()) {


                                    TechnoparkUser post = response.body();

                                    /*
                                    editor.putString(AUTH_TOKEN_EXTRA, post.getAuthToken());                 // such we planning to save data
                                    editor.apply();*/
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("KEY", post.getAuthToken());                       // temporary solution
                                    startActivity(intent);

                                }
                                else {
                                    result.setText("Что-то не так! Вероятно, неправильно указаны данные");
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<TechnoparkUser> call, @NonNull Throwable t) {
                                result.setText("Нет соединения!");
                            }
                        });
            }
        });

    }


    public static String sha256(String base) { //Algorithm for SHA256
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }


}

