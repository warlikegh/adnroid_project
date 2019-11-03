package com.example.technoparkmobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button enter;
    TextView quq;
    UserAuth mData;
    EditText mLogin;
    EditText mPassword;
    TechnoparkUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quq = findViewById(R.id.result);
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);
        enter = findViewById(R.id.getBtn);


        mData = new UserAuth();
        mUser = new TechnoparkUser();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mLogin.getText().toString();
                String pass = mPassword.getText().toString();
                String req = login.substring(0,1)+pass.substring(2,3)+
                        login.substring(1,2)+pass.substring(1,2)+
                        login.substring(2,3)+pass.substring(0,1);
                String salt = "";

                mData.setLogin(login);
                mData.setPassword(pass);
                mData.setReqId(req);
                mData.setToken(sha256(req+salt));

                quq.append(login+"\n"+pass+"\n"+req+"\n"+sha256(req+salt));



                Call<TechnoparkUser> u =NetworkService.getInstance()
                        .getJSONApi()
                        .getPostData(mData);
                        u.enqueue(new Callback<TechnoparkUser>() {
                            @Override
                            public void onResponse(@NonNull Call<TechnoparkUser> call, @NonNull Response<TechnoparkUser> response) {
                                TechnoparkUser post = response.body();
                 /*
                                if (post.getAuthToken()!=null)
                                {
                                quq.append(post.getAuthToken() + "\n");
                                quq.append(post.getUserId() + "\n");
                                quq.append(post.getUsername() + "\n");
                                }*/
                            }

                            @Override
                            public void onFailure(@NonNull Call<TechnoparkUser> call, @NonNull Throwable t) {

                                quq.append("Error occurred while getting request!");
                                t.printStackTrace();
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

