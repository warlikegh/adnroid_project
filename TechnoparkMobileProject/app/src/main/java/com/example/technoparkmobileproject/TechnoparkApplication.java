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

        import static com.example.technoparkmobileproject.AuthActivity.AUTH_TOKEN;

public class TechnoparkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

/*
        SharedPreferences sp = getSharedPreferences("my_settings",
                Context.MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        if (!hasVisited) {
            // выводим нужную активность
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit(); // не забудьте подтвердить изменения
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        } else
            {
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
                                startActivity(intent);
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
