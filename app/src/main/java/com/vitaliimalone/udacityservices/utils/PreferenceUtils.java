package com.vitaliimalone.udacityservices.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String KEY_WATER_COUNT = "water-count";
    public static final String KEY_CHARGING_COUNT = "charging-count";

    private static final int DEFAULT_COUNT = 0;

    synchronized private static void setWaterCount(Context context, int glassesOfWater) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_WATER_COUNT, glassesOfWater);
        editor.apply();
    }

    public static int getWaterCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
    }

    synchronized public static void incrementWaterCount(Context context) {
        int waterCount = getWaterCount(context);
        setWaterCount(context, ++waterCount);
    }

    public static int getChargingCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingCount = prefs.getInt(KEY_CHARGING_COUNT, DEFAULT_COUNT);
        return chargingCount;
    }

    synchronized public static void incrementChargingCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingCount = prefs.getInt(KEY_CHARGING_COUNT, DEFAULT_COUNT);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CHARGING_COUNT, ++chargingCount);
        editor.apply();
    }
}
