package com.vitaliimalone.udacityservices.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.vitaliimalone.udacityservices.sync.WaterReminderFirebaseJobService;

import java.util.concurrent.TimeUnit;

public class ReminderUtils {
    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));

    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";

    private static boolean isInitialized;

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {
        if (isInitialized) return;
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job reminderJob = dispatcher.newJobBuilder()
                .setService(WaterReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, 10))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(reminderJob);
        isInitialized = true;

    }
}
