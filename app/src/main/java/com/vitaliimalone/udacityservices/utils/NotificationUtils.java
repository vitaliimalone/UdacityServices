package com.vitaliimalone.udacityservices.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.vitaliimalone.udacityservices.MainActivity;
import com.vitaliimalone.udacityservices.R;

public class NotificationUtils {

    private static final int WATER_REMINDER_NOTIFICATION_ID = 101;
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 201;

    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    public static void remindUserBecauseCharging(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notification);

    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.glass_of_water);
    }
}
