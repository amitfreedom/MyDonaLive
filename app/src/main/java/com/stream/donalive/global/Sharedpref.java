package com.stream.donalive.global;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Sharedpref {
    private final Context context;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences sharedpreferences;


    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key) {
        return sharedpreferences.getString(key, "");
    }

    public void saveBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedpreferences.getBoolean(key, false);
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }

    public <T> T getModel(String key, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(sharedpreferences.getString(key, ""), type);
    }


    public Sharedpref(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(AppConstants.DB, Context.MODE_PRIVATE);
        prefsEditor = sharedpreferences.edit();
    }

    public void saveModel(String key, Object modelClass) {
        Gson gson = new Gson();
        prefsEditor.putString(key, gson.toJson(modelClass));
        prefsEditor.apply();
    }

    public void login(Context context, boolean connected) {
        sharedpreferences = context.getSharedPreferences(AppConstants.DB, Context.MODE_PRIVATE);
        prefsEditor = sharedpreferences.edit();
        prefsEditor.putBoolean("connected", connected);
        prefsEditor.commit();
    }

    public boolean isLogin(Context context) {
        return sharedpreferences.getBoolean("connected", false);
    }


}
