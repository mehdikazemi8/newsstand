package com.nebeek.newsstand.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.nebeek.newsstand.data.models.BaseModel;

import java.lang.reflect.Type;

// todo, for all operations use asynctask

public class PreferenceManager {
    private enum Key {
    }

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }

        return instance;
    }

    private PreferenceManager(Context context) {
        init(context);
    }

    private static PreferenceManager instance = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences("veevapp", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void put(Key key, Integer value) {
        editor.putInt(key.toString(), value);
        editor.apply();
    }

    private void put(Key key, String value) {
        editor.putString(key.toString(), value);
        editor.apply();
    }

    private void put(Key key, Float value) {
        editor.putFloat(key.toString(), value);
        editor.apply();
    }

    private void put(Key key, Boolean value) {
        editor.putBoolean(key.toString(), value);
        editor.apply();
    }

    private void put(Key key, BaseModel object) {
        editor.putString(key.toString(), object.serialize());
        editor.apply();
    }

    private String getString(Key key) {
        return sharedPreferences.getString(key.toString(), null);
    }

    private Integer getInt(Key key) {
        return sharedPreferences.getInt(key.toString(), 0);
    }

    private Boolean getBoolean(Key key) {
        return sharedPreferences.getBoolean(key.toString(), false);
    }

    private Float getFloat(Key key) {
        return sharedPreferences.getFloat(key.toString(), 0f);
    }

    private <T> T get(Key key, Type type) {
        String json = sharedPreferences.getString(key.toString(), null);
        if (json == null) {
            return null;
        }

        return BaseModel.deserialize(json, type);
    }

    private void remove(Key key) {
        editor.remove(key.toString());
        editor.apply();
    }
}

