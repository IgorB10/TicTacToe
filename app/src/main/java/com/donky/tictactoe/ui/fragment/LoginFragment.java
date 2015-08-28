package com.donky.tictactoe.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        String displayName = AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getDisplayName();
        if (userId != null && displayName != null){
            mLoginButton.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            goToGameActvity();
        }else {
            mLoginContent.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_login)
    public void loginButton(){
        mProgressBar.setVisibility(View.VISIBLE);
        AppTicTakToe.getsAppTicTakToe().getPreferencesManager().setUserId(editTextUserId.getText().toString());
        AppTicTakToe.getsAppTicTakToe().getPreferencesManager().setDisplayName(editTextUserName.getText().toString());
        login();
    }

    private void buttonState(boolean isEnable){
        editTextUserId.setEnabled(isEnable);
        editTextUserName.setEnabled(isEnable);
    }

    private void login(){
        buttonState(false);
        User user = new User(AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId(),
                AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getDisplayName());
        AppTicTakToe.getsAppTicTakToe().updateUserDetails(user, new CallBack() {
            @Override
            public void success(Object response) {
                goToGameActvity();
            }

            @Override
            public void error(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                buttonState(true);
                mLoginContent.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void goToGameActvity(){
        Intent intent = new Intent(getActivity(), GamesActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
