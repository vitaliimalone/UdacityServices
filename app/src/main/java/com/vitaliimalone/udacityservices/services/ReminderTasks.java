package com.vitaliimalone.udacityservices.services;

import android.content.Context;

import com.vitaliimalone.udacityservices.utils.PreferenceUtils;

public class ReminderTasks {

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    public static void executeTask(Context context, String action) {
        if (action.equals(ACTION_INCREMENT_WATER_COUNT)) {
            incrementWaterCount(context);
        }
    }

    private static void incrementWaterCount(Context context) {
        PreferenceUtils.incrementWaterCount(context);
    }
}
