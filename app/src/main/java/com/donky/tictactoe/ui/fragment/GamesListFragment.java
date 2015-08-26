package com.donky.tictactoe.ui.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donky.tictactoe.R;
import com.donky.tictactoe.ui.dialog.AddGameDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class GamesListFragment extends BaseFragment {

    @Bind(R.id.fab_add_game)
    FloatingActionButton actionButtonAddGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.games_listfragement, container, false);
    }

    @OnClick(R.id.fab_add_game)
    protected void addGame(){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("AddGameDialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            DialogFragment newFragment = new AddGameDialog();
            newFragment.show(ft, "AddGameDialog");
    }
}
