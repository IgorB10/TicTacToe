package com.donky.tictactoe.ui;

import android.util.Log;

import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;

import java.util.Map;

public class AppDonkyListener implements DonkyListener {

    private static final String TAG = AppDonkyListener.class.getSimpleName();

    private String mClassName;

    public AppDonkyListener(String mClassName) {
        this.mClassName = mClassName;
    }

    @Override
    public void success() {
        Log.d(TAG, mClassName + " success");

    }

    @Override
    public void error(DonkyException e, Map<String, String> map) {
        Log.d(TAG, mClassName + " " + e.getMessage());

    }
}
