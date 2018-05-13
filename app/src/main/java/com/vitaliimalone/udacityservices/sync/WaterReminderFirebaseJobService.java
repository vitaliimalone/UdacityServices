package com.vitaliimalone.udacityservices.sync;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class WaterReminderFirebaseJobService extends JobService {

    private AsyncTask backgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                ReminderTasks.executeTask(
                        WaterReminderFirebaseJobService.this,
                        ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (backgroundTask != null) backgroundTask.cancel(true);
        return true;
    }
}
