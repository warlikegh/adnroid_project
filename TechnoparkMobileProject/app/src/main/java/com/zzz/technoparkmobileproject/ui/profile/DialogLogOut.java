package com.zzz.technoparkmobileproject.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zzz.technoparkmobileproject.R;
import com.zzz.technoparkmobileproject.SecretData;
import com.zzz.technoparkmobileproject.TechnoparkApplication;
import com.zzz.technoparkmobileproject.auth.AuthActivity;
import com.zzz.technoparkmobileproject.ui.shedule.ScheduleViewModel;

import static android.content.Context.MODE_PRIVATE;
import static com.my.tracker.MyTracker.trackEvent;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.CREATE_FIRST_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_AUTHORISED;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_NEWS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_PROFILE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_SCHEDULE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.LOGIN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PASSWORD;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SITE;
import static com.zzz.technoparkmobileproject.auth.AuthRepo.deleteTokenFromServer;

public class DialogLogOut {
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
                deleteTokenFromServer(context);

                SharedPreferences mSecretSettings = new SecretData().getSecretData(context);
                trackEvent("Log  out");
                SharedPreferences.Editor mSecretEditor = mSecretSettings.edit();
                mSecretEditor.remove(LOGIN)
                        .remove(PASSWORD)
                        .remove(SITE)
                        .putBoolean(IS_AUTHORISED, false)
                        .apply();

                SharedPreferences mSettings = context.getSharedPreferences(CREATE_FIRST_SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSettings.edit();
                mEditor.putBoolean(IS_FIRST_NEWS, true)
                        .putBoolean(IS_FIRST_SCHEDULE, true)
                        .putBoolean(IS_FIRST_PROFILE, true)
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