package com.zzz.technoparkmobileproject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.FIREBASE_ID;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.FIREBASE_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.FIREBASE_TOKEN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_AUTHORISED;
import static com.zzz.technoparkmobileproject.auth.AuthRepo.deleteTokenFromServer;
import static com.zzz.technoparkmobileproject.auth.AuthRepo.sendTokenToServer;

public class MessagingService extends FirebaseMessagingService {

    public static final String TAG = "Firebase";
    private static final int NOTIFICATION_ID_SIMPLE = 4;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + body);

            showMessageNotification(title, body);

        }
    }

    @Override
    public void onNewToken(final String token) {
        super.onNewToken(token);
        Log.d(MessagingService.TAG, "Refreshed token: " + token);

        SharedPreferences mSettings = getSharedPreferences(FIREBASE_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();

        editor.putString(FIREBASE_TOKEN, token).apply();

        if (!mSettings.contains(FIREBASE_ID))
            editor.putString(FIREBASE_ID, UUID.randomUUID().toString()).apply();

        SharedPreferences mSecretSettings = new SecretData().getSecretData(this);
        if (mSecretSettings.getBoolean(IS_AUTHORISED, false)) {
            deleteTokenFromServer(this);
            sendTokenToServer(this);
        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(FIREBASE_SETTINGS, MODE_PRIVATE).getString(FIREBASE_TOKEN, "empty");
    }

    public static String getID(Context context) {
        return context.getSharedPreferences(FIREBASE_SETTINGS, MODE_PRIVATE).getString(FIREBASE_ID, "empty");
    }


    public void showMessageNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null)
            return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_DEFAULT);

        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        manager.notify(NOTIFICATION_ID_SIMPLE, builder.build());
    }


}

