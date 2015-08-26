package com.donky.tictactoe.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.donky.tictactoe.R;

public class TicTakToeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictaktoe_activity);
    }
}
