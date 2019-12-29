package com.example.technoparkmobileproject.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.TechnoparkApplication;
import com.example.technoparkmobileproject.auth.AuthActivity;
import com.example.technoparkmobileproject.ui.shedule.ScheduleViewModel;

public class DialogLogOut {

    static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";
    private static String SITE = "site";
    private static ProfileViewModel mProfileViewModel;
    private static ScheduleViewModel mScheduleViewModel;

    public static AlertDialog getDialog(final Context context) {
        mProfileViewModel = new ProfileViewModel(TechnoparkApplication.from(context));
        mScheduleViewModel = new ScheduleViewModel(TechnoparkApplication.from(context));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.answer_log_out);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences mSecretSettings = new SecretData().getSecretData(context);
                SharedPreferences.Editor mSecretEditor = mSecretSettings.edit();
                mSecretEditor.remove(LOGIN)
                        .remove(PASSWORD)
                        .remove(AUTH_TOKEN)
                        .remove(SITE)
                        .apply();

                SharedPreferences mSettings = context.getSharedPreferences("createFirst", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSettings.edit();
                mEditor.putBoolean("isFirstNews", true)
                        .putBoolean("isFirstSchedule", true)
                        .putBoolean("isFirstProfile", true)
                        .apply();

                // mProfileViewModel.cleanDB();
                mScheduleViewModel.cleanDB();

                context.startActivity(new Intent(context, AuthActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        });
        builder.setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();

    }
}