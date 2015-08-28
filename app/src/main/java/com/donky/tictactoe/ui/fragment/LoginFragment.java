package com.donky.tictactoe.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.donky.tictactoe.AppTicTakToe;
import com.donky.tictactoe.R;
import com.donky.tictactoe.model.User;
import com.donky.tictactoe.network.CallBack;
import com.donky.tictactoe.ui.activity.GamesActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {


    @Bind(R.id.btn_login)Button mLoginButton;
    @Bind(R.id.et_user_id)EditText editTextUserId;
    @Bind(R.id.et_user_name)EditText editTextUserName;
    @Bind(R.id.progress)ProgressBar mProgressBar;
    @Bind(R.id.login_content)LinearLayout mLoginContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragemnt, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userId = AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId();
        if (userId != null){
            mLoginButton.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            login();
        }else {
            mLoginContent.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_login)
    public void loginButton(){
        mProgressBar.setVisibility(View.VISIBLE);
//        AppTicTakToe.getsAppTicTakToe().getPreferencesManager().setUserId("test_device_1");
//        AppTicTakToe.getsAppTicTakToe().getPreferencesManager().setDisplayId("Igor");
        AppTicTakToe.getsAppTicTakToe().getPreferencesManager().setUserId(editTextUserId.getText().toString());
        AppTicTakToe.getsAppTicTakToe().getPreferencesManager().setDisplayId(editTextUserName.getText().toString());
        login();
    }



    private void login(){
        User user = new User(AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId(),
                AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getDisplayId());
        AppTicTakToe.getsAppTicTakToe().initDonkyModel(user, new CallBack() {
            @Override
            public void success(Object response) {
                Intent intent = new Intent(getActivity(), GamesActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void error(String message) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }
}
