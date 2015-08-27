package com.donky.tictactoe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.donky.tictactoe.AppTicTakToe;
import com.donky.tictactoe.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }
}
