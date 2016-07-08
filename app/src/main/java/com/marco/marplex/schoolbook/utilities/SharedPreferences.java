package com.marco.marplex.schoolbook.utilities;

/**
 * Created by marco on 28/08/15.
 */

import android.content.Context;

import java.util.Map;

public class SharedPreferences {

    public static int incrementNumber(Context c, String prefName, String key) {
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        android.content.SharedPreferences.Editor editor = settings.edit();
        int number = Integer.parseInt(settings.getString(key, "0")) + 1;
        editor.putString(key, ""+number);
        editor.commit();
        return number++;
    }

    public static int decrementNumber(Context c, String prefName, String key) {
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        android.content.SharedPreferences.Editor editor = settings.edit();
        int number = Integer.parseInt(settings.getString(key, "0"));
        editor.putString(key, ""+number--);
        editor.commit();
        return number--;
    }

    public static void saveString(Context c, String prefName, String key, String i) {
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        android.content.SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, i);
        editor.commit();
    }

    public static Map<String,?> getAllKeys(Context c, String prefName){
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return settings.getAll();
    }

    public static String loadString(Context c, String prefName, String key) {
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return settings.getString(key, null);
    }

    public static boolean loadBoolean(Context c, String prefName, String key) {
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return settings.getBoolean(key, false);
    }

    public static void saveBoolean(Context c, String prefName, String key, boolean i) {
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        android.content.SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, i);
        editor.commit();
    }

    public static boolean keyExist(Context c, String prefName, String key){
        return c.getSharedPreferences(prefName, 0).contains(key);
    }

    public static void remove(Context c, String prefName, String key) {
        android.content.SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        if (settings.contains(key)) {
            android.content.SharedPreferences.Editor editor = settings.edit();
            editor.remove(key);
            editor.commit();
        }
    }
}
