package com.example.technoparkmobileproject;



import android.app.Application;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;

        import androidx.annotation.NonNull;

        import java.math.BigInteger;
        import java.util.Random;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        import static com.example.technoparkmobileproject.AuthActivity.AUTH_TOKEN_EXTRA;

public class TechnoparkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
/*
        String userLogin;
        String userPassword;

        SharedPreferences sp = getSharedPreferences("my_settings",
                Context.MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
            startActivity(new Intent(getApplicationContext(), AuthActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else
            {/*
                startActivity(new Intent(getApplicationContext(), AuthActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));





        String login = AuthActivity.prefs.getString("login", "");
        String pass = AuthActivity.prefs.getString("password", "");
        String req = new BigInteger(16 * 4, new Random()).toString(16);
        String salt = "";
        UserAuth mData = new UserAuth();
        if ((login!=null) && (pass !=null)) {
            mData.setLogin(login);
            mData.setPassword(pass);
            mData.setReqId(req);
            mData.setToken(AuthActivity.sha256(req + salt));
            NetworkService.getInstance()
                    .getJSONApi()
                    .getPostData(mData)
                    .enqueue(new Callback<TechnoparkUser>() {
                        @Override
                        public void onResponse(@NonNull Call<TechnoparkUser> call, @NonNull Response<TechnoparkUser> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra(AUTH_TOKEN, response.body().getAuthToken());
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else {
                                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<TechnoparkUser> call, @NonNull Throwable t) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
        }

        }*/
    }
}
