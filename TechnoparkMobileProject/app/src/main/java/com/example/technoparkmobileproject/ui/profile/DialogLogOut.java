package com.example.technoparkmobileproject.ui.profile;
/*
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.TechnoparkApplication;
import com.example.technoparkmobileproject.auth.AuthActivity;
import com.example.technoparkmobileproject.ui.shedule.ScheduleViewModel;

import java.util.Objects;

public class DialogLogOut extends DialogFragment implements OnClickListener {

    static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";
    private static String SITE = "site";
    Context context;
    private static ProfileViewModel mProfileViewModel;
    private static ScheduleViewModel mScheduleViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_log_out, null);
        v.findViewById(R.id.btnYes).setOnClickListener(this);
        v.findViewById(R.id.btnNo).setOnClickListener(this);
        context = getContext();
        mProfileViewModel = new ProfileViewModel(TechnoparkApplication.from(context));
        mScheduleViewModel =  new ScheduleViewModel(TechnoparkApplication.from(context));
        return v;
    }

    public void onClick(View v) {
     //   Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
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

        getContext().startActivity(new Intent(context, AuthActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.d(LOG_TAG, "Dialog 1: onDismiss");
        dismiss();
    }

}
*/

import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.DialogFragment;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.Button;

        import androidx.fragment.app.FragmentManager;
        import androidx.lifecycle.ViewModelProvider;

        import com.example.technoparkmobileproject.R;
        import com.example.technoparkmobileproject.SecretData;
        import com.example.technoparkmobileproject.TechnoparkApplication;
        import com.example.technoparkmobileproject.auth.AuthActivity;
        import com.example.technoparkmobileproject.ui.shedule.ScheduleViewModel;

        import java.util.Objects;

public class DialogLogOut {

    static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";
    private static String SITE = "site";
    Context context;
    private static ProfileViewModel mProfileViewModel;
    private static ScheduleViewModel mScheduleViewModel;

    public static AlertDialog getDialog(final Context context) {
        mProfileViewModel = new ProfileViewModel(TechnoparkApplication.from(context));
        mScheduleViewModel =  new ScheduleViewModel(TechnoparkApplication.from(context));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.answer_log_out);
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