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
import com.vitaliimalone.udacityservices.sync.ReminderTasks;
import com.vitaliimalone.udacityservices.sync.WaterReminderIntentService;

public class NotificationUtils {

    private static final int WATER_REMINDER_NOTIFICATION_ID = 101;
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 201;

    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    private static final int ACTION_DRINK_PENDING_INTENT_ID = 11;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 21;

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
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true)
                .build();

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notification);

    }

    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, WaterReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(
                R.drawable.ic_cancel_black,
                context.getString(R.string.ignore_reminder_action),
                ignoreReminderPendingIntent);

        return ignoreReminderAction;
    }

    private static NotificationCompat.Action drinkWaterAction(Context context) {
        Intent incrementWater = new Intent(context, WaterReminderIntentService.class);
        incrementWater.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWater,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action incrementWaterAction = new NotificationCompat.Action(
                R.drawable.ic_drink_water_black,
                context.getString(R.string.increment_water_action),
                incrementWaterPendingIntent);

        return incrementWaterAction;
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
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
