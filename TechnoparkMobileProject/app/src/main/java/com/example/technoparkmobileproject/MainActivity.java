package com.example.technoparkmobileproject;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView quq;
    UserAuth mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quq = findViewById(R.id.result);
        mData = new UserAuth();

        mData.setLogin("aa_tema@mail.ru");
        mData.setPassword("qwerty3673ap");
        mData.setReqId("1");
        mData.setToken(sha256("12"));

        NetworkService.getInstance()
                .getJSONApi()
                .getPostData(mData)
                .enqueue(new Callback<TechnoparkUser>() {
                    @Override
                    public void onResponse(@NonNull Call<TechnoparkUser> call, @NonNull Response<TechnoparkUser> response) {
                        TechnoparkUser post = response.body();

                        quq.append(post.getAuthToken() + "\n");
                        quq.append(post.getUserId() + "\n");
                        quq.append(post.getUsername() + "\n");
                    }

                    @Override
                    public void onFailure(@NonNull Call<TechnoparkUser> call, @NonNull Throwable t) {

                        quq.append("Error occurred while getting request!");
                        t.printStackTrace();
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

