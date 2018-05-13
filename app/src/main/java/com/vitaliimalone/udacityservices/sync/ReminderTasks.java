package com.vitaliimalone.udacityservices.sync;

import android.content.Context;

import com.vitaliimalone.udacityservices.utils.NotificationUtils;
import com.vitaliimalone.udacityservices.utils.PreferenceUtils;

public class ReminderTasks {

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_CHARGING_REMINDER = "charging-reminder";

    public static void executeTask(Context context, String action) {
        if (action.equals(ACTION_INCREMENT_WATER_COUNT)) {
            PreferenceUtils.incrementWaterCount(context);
            NotificationUtils.clearAllNotifications(context);
        } else if (action.equals(ACTION_DISMISS_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (action.equals(ACTION_CHARGING_REMINDER)) {
            PreferenceUtils.incrementChargingCount(context);
            NotificationUtils.remindUserBecauseCharging(context);

        }
    }
}
