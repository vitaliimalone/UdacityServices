package com.vitaliimalone.udacityservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vitaliimalone.udacityservices.sync.ReminderTasks;
import com.vitaliimalone.udacityservices.sync.WaterReminderIntentService;
import com.vitaliimalone.udacityservices.utils.NotificationUtils;
import com.vitaliimalone.udacityservices.utils.PreferenceUtils;
import com.vitaliimalone.udacityservices.utils.ReminderUtils;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView waterCountTv;
    private TextView chargingCountTv;
    private ImageButton waterIncrementButton;
    private ImageView chargingIv;

    private Toast toast;
    private IntentFilter chargingIntentFilter;
    private BroadcastReceiver chargingBroadcastReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waterCountTv = findViewById(R.id.water_increment_count_tv);
        chargingCountTv = findViewById(R.id.charging_count_tv);
        waterIncrementButton = findViewById(R.id.water_increment_ib);
        chargingIv = findViewById(R.id.charging_iv);

        waterIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementWater();
            }
        });

        updateWaterCount();
        updateChargingCount();

        ReminderUtils.scheduleChargingReminder(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        chargingIntentFilter = new IntentFilter();
        chargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        chargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        chargingBroadcastReciever = new ChargingBroadcastReciever();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            showCharging(batteryManager.isCharging());
        } else {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent currentBatteryStatusIntent = registerReceiver(null, intentFilter);
            int batteryStatus = currentBatteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                    batteryStatus == BatteryManager.BATTERY_STATUS_FULL;
            showCharging(isCharging);
        }


        registerReceiver(chargingBroadcastReciever, chargingIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(chargingBroadcastReciever);
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

    private void showCharging(boolean isCharging) {
        if (isCharging) {
            chargingIv.setImageResource(R.drawable.ic_battery_charging_red_24dp);
        } else {
            chargingIv.setImageResource(R.drawable.ic_battery_charging_full_black_24dp);
        }
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

    private class ChargingBroadcastReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = (action.equals(Intent.ACTION_POWER_CONNECTED));
            showCharging(isCharging);
        }
    }
}
