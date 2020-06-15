package com.xapps.karbala.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import com.xapps.karbala.App;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.source.preferences.SharedManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;


public class NotificationUtils {

    private static NotificationManager mNotificationManager;
    private static NotificationCompat.Builder mNotificationBuilder;

    private static Class<?> target;
    private static int referenceID;


    private final static AtomicInteger c =
            new AtomicInteger(SharedManager.newInstance().getNotificationIdCounter(Constants.NOTIFICATION_ID_COUNTER));

    private static int notificationId;


    public static void setID() {
        notificationId = c.incrementAndGet();
        SharedManager.newInstance().saveNotificationIdCounter(notificationId);

    }


    public static void showNotification(JSONObject message) {
        showNotification(App.mContext.getString(R.string.app_name), message);
    }


    public static void showNotification(String title, JSONObject message1) {
        setID();
        mNotificationManager = (NotificationManager) App.mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            referenceID = message1.getInt("ReferenceId");
            // new question or replay from student -> open course details but make QA select not lectures
            if (referenceID == 1 || referenceID == 2 || referenceID == 3) {
                //target = CourseDetailsActivity.class;
            }
            // open notificationsActivity
            else if (referenceID == 4) {
                //target = NotificationsActivity.class;
            } else if (referenceID == 5) {
               // HomeActivity.selectedFragmentId = 2;
                //target = HomeActivity.class;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        PendingIntent pendingIntent = null;
        if (target != null) {
            Intent intent = new Intent(App.mContext, target);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                // new question or replay from student -> open course details but make QA select not lectures
                if (referenceID == 1 || referenceID == 2 || referenceID == 3) {
                    //intent.putExtra(Constants.COURSE_ID, "CourseId");
                }

            } catch (Exception e) {

            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            pendingIntent = PendingIntent.getActivity(App.mContext
                    , 0
                    , intent
                    , PendingIntent.FLAG_ONE_SHOT);
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        String messaage = "";
        try {
            messaage = message1.getString("message");
        } catch (Exception e) {
        }
        mNotificationBuilder = new NotificationCompat.Builder(App.mContext, Constants.NOTIFICATIONChANEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(App.mContext.getResources().getString(R.string.app_name))
                .setContentText(messaage)
                .setAutoCancel(true)
                .setSound(soundUri);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            CharSequence channelName = Constants.NOTIFICATIONChANEL;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(Constants.NOTIFICATIONChANEL, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setSound(soundUri, null);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationBuilder.setChannelId(Constants.NOTIFICATIONChANEL);
        } else {

            mNotificationBuilder.setAutoCancel(true)
                    .setSound(soundUri)
                    .setLights(Color.BLUE, 500, 500)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000});

        }

        if (pendingIntent != null)
            mNotificationBuilder.setContentIntent(pendingIntent);

        mNotificationManager.notify(notificationId, mNotificationBuilder.build());
    }

}