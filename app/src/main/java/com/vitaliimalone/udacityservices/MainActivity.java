package com.vitaliimalone.udacityservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vitaliimalone.udacityservices.services.ReminderTasks;
import com.vitaliimalone.udacityservices.services.WaterReminderIntentService;
import com.vitaliimalone.udacityservices.utils.NotificationUtils;
import com.vitaliimalone.udacityservices.utils.PreferenceUtils;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView waterCountTv;
    private TextView chargingCountTv;
    private ImageButton waterIncrementButton;

    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waterCountTv = findViewById(R.id.water_increment_count_tv);
        chargingCountTv = findViewById(R.id.charging_count_tv);
        waterIncrementButton = findViewById(R.id.water_increment_ib);

        updateWaterCount();
        updateChargingCount();

        waterIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementWater();
            }
        });

        findViewById(R.id.test_notification_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtils.remindUserBecauseCharging(MainActivity.this);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void updateWaterCount() {
        int waterCount = PreferenceUtils.getWaterCount(this);
        waterCountTv.setText(waterCount+"");
    }

    private void updateChargingCount() {
        int chargingCount = PreferenceUtils.getChargingCount(this);
        String formattedChargingCount = getResources().getString(R.string.charge_notification_count, chargingCount);
        chargingCountTv.setText(formattedChargingCount);
    }

    public void incrementWater() {
        if (toast != null) toast.cancel();

        toast = Toast.makeText(this, R.string.drink_water_toast, Toast.LENGTH_SHORT);
        toast.show();

        Intent incrementWaterCountIntent = new Intent(this, WaterReminderIntentService.class);
        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        startService(incrementWaterCountIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferenceUtils.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
        } else if (PreferenceUtils.KEY_CHARGING_COUNT.equals(key)) {
            updateChargingCount();
        }
    }
}
