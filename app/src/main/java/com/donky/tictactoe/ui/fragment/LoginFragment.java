package com.donky.tictactoe.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.donky.tictactoe.AppTicTakToe;
import com.donky.tictactoe.R;
import com.donky.tictactoe.model.User;
import com.donky.tictactoe.network.CallBack;
import com.donky.tictactoe.ui.activity.GamesListActivity;
import com.donky.tictactoe.ui.activity.TicTakToeActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {


    @Bind(R.id.et_user_id)EditText editTextUserId;
    @Bind(R.id.et_user_name)EditText editTextUserName;
    @Bind(R.id.progress)ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragemnt, container, false);
    }


    @OnClick(R.id.btn_login)
    public void login(){
        mProgressBar.setVisibility(View.VISIBLE);
        User user = new User("test_device_1", "Igor");
//        User user = new User(editTextUserId.getEditableText().toString(), editTextUserName.getEditableText().toString());
        AppTicTakToe.getsAppTicTakToe().initDonkyModel(user, new CallBack() {
            @Override
            public void success(Object response) {
                Intent intent = new Intent(getActivity(), GamesListActivity.class);
                startActivity(intent);
            }

            @Override
            public void error(String message) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
