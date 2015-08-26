package com.donky.tictactoe.ui.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.donky.tictactoe.R;
import com.donky.tictactoe.tictactoe.GameManager;
import com.donky.tictactoe.tictactoe.GameSession;
import com.donky.tictactoe.ui.dialog.AddGameDialog;

import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;
import net.donky.core.network.DonkyNetworkController;
import net.donky.core.network.content.ContentNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class GamesListFragment extends BaseFragment implements GameManager.Game {

    private GameManager mGameManager;

    @Bind(R.id.fab_add_game)
    FloatingActionButton actionButtonAddGame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameManager = new GameManager(this);
    }

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

    public void sendInvite(String userId){
        mGameManager.sendInvite(userId);
    }

    @Override
    public void onCreateGame(GameSession gameSession) {

    }

    @Override
    public void onFinishGame(GameSession gameSession) {

    }
}
