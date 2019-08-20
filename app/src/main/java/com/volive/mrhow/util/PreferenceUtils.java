package com.volive.mrhow.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class PreferenceUtils {
    public static String Language = "language";
    public static String userid = "userid";
    public static String userimage = "userimage";
    public static String username = "username";
    public static String LOGIN = "LOGIN";
    public static String enteremail ="enteremail";
    public static String  enterpassword = "enterpassword";public static String strname = "strname";
    public static String strvideo = "strvideo";
    public static String backgroundvideo="backgroundvideo";


    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    public PreferenceUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        edit = preferences.edit();
    }

    public String getLanguage() {
        return preferences.getString(Language, "");
    }

    public void setLanguage(String lang) {
        edit.putString(Language, lang);
        edit.commit();
    }

    public void saveString(String strKey, String strValue) {
        edit.putString(strKey, strValue);
        edit.commit();
    }

    public void saveInt(String strKey, int value) {
        edit.putInt(strKey, value);
        edit.commit();
    }

    public void saveLong(String strKey, Long value) {
        edit.putLong(strKey, value);
        edit.commit();
    }

    public void saveFloat(String strKey, float value) {
        edit.putFloat(strKey, value);
        edit.commit();
    }

    public void saveDouble(String strKey, String value) {
        edit.putString(strKey, value);
        edit.commit();
    }

    public void saveBoolean(String strKey, boolean value) {
        edit.putBoolean(strKey, value);
        edit.commit();
    }

    public void removeFromPreference(String strKey) {
        edit.remove(strKey);
    }

    public String getStringFromPreference(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getbooleanFromPreference(String strKey, boolean defaultValue) {
        return preferences.getBoolean(strKey, defaultValue);
    }

    public int getIntFromPreference(String strKey, int defaultValue) {
        return preferences.getInt(strKey, defaultValue);
    }

    public long getLongFromPreference(String strKey) {
        return preferences.getLong(strKey, 0);
    }

    public float getFloatFromPreference(String strKey, float defaultValue) {
        return preferences.getFloat(strKey, defaultValue);
    }

    public double getDoubleFromPreference(String strKey, double defaultValue) {
        return Double.parseDouble(preferences.getString(strKey, "" + defaultValue));
    }

    public void logOut() {
        edit.remove(userid);
        edit.clear();
        edit.commit();

    }
}
