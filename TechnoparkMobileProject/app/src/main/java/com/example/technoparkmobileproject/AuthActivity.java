package com.example.technoparkmobileproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    Button enter;
    TextView quq;
    UserAuth mData;
    EditText mLogin;
    EditText mPassword;
    TechnoparkUser mUser;
    public static  SharedPreferences prefs;
    public static final String AUTH_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AuthTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_main);

        quq = findViewById(R.id.result);
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);
        enter = findViewById(R.id.getBtn);


        mData = new UserAuth();
        mUser = new TechnoparkUser();

        prefs = this.getSharedPreferences(
                "com.example.technoparkmobileproject", Context.MODE_PRIVATE);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mLogin.getText().toString();
                String pass = mPassword.getText().toString();
                String req = new BigInteger(16 * 4, new Random()).toString(16);                       //need to rewrite req
                String salt = "";

                mData.setLogin(login);
                mData.setPassword(pass);
                mData.setReqId(req);
                mData.setToken(sha256(req+salt));

                //quq.append(login+"\n"+pass+"\n"+req+"\n"+sha256(req+salt));      //delete

                NetworkService.getInstance()
                        .getJSONApi()
                        .getPostData(mData)
                        .enqueue(new Callback<TechnoparkUser>() {
                            @Override
                            public void onResponse(@NonNull Call<TechnoparkUser> call, @NonNull Response<TechnoparkUser> response) {
                                if (response.isSuccessful()) {
                                    prefs.edit().putString("login", mLogin.getText().toString()).apply();
                                    prefs.edit().putString("pass", mLogin.getText().toString()).apply();
                                    TechnoparkUser post = response.body();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra(AUTH_TOKEN, response.body().getAuthToken());
                                    startActivity(intent);
                                }
                                else {
                                    quq.append("Что-то не так! Вероятно, неправильно указаны данные");
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<TechnoparkUser> call, @NonNull Throwable t) {
                                quq.append("Нет соединения!");
                            }
                        });
            }
        });

    }


    public static String sha256(String base) {
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

