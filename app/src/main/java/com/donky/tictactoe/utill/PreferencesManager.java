package com.donky.tictactoe.utill;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

public class PreferencesManager {



    private static final String SHARED_PREFERENCES = "com.donky.tictactoe";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DISPLAY_ID = "display_id";

    private SharedPreferences mSharedPreferences;

    public PreferencesManager(Application application) {
        mSharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES, Application.MODE_PRIVATE);
    }

    public String getUserId() {
        return mSharedPreferences.getString(KEY_USER_ID, null);
    }

    @SuppressLint("CommitPrefEdits")
    public void setUserId(String userId) {
        mSharedPreferences.edit().putString(KEY_USER_ID, userId).commit();
    }

    public String getDisplayId() {
        return mSharedPreferences.getString(KEY_DISPLAY_ID, null);
    }

    @SuppressLint("CommitPrefEdits")
    public void setDisplayId(String displayId) {
        mSharedPreferences.edit().putString(KEY_DISPLAY_ID, displayId).commit();
    }
}
