package com.donky.tictactoe.tictactoe;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import com.donky.tictactoe.AppTicTakToe;
import com.donky.tictactoe.NotificationManager;
import com.donky.tictactoe.model.Invite;
import com.donky.tictactoe.model.Move;
import com.donky.tictactoe.ui.view.GameView.State;
import com.donky.tictactoe.utill.Constants;
import com.google.gson.Gson;

import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;
import net.donky.core.network.DonkyNetworkController;
import net.donky.core.network.content.ContentNotification;

import org.json.JSONException;
import org.json.JSONObject;

public class GameSession implements GameMoves{

    private Invite mInvite;
    private State[] states;

    public GameSession(Invite invite){
        mInvite = invite;
        initStates();
        NotificationManager.getInstance().addListener(Constants.GAME + invite.getGameId(), new NotificationManager.OnNotificationListener<Move>() {
            @Override
            public void notifyObservers(Move move) {
                receive(move);
            }
        });
    }

    @Override
    public void move(Move move) {
        sendMove(move);
    }

    @Override
    public void receive(Move move) {
        states[move.getPosition()] = State.PLAYER2;
    }

    public Invite getmInvite() {
        return mInvite;
    }

    public void setmInvite(Invite mInvite) {
        this.mInvite = mInvite;
    }

    public State[] getStates() {
        return states;
    }

    public void setStates(State[] states) {
        this.states = states;
    }

    private void sendMove(final Move move){
        String jsonString = new Gson().toJson(move);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        }catch (JSONException e){
            e.printStackTrace();
        }
        ContentNotification contentNotification =
                new ContentNotification(mInvite.getToUserId(), Constants.INVITE, jsonObject);

        DonkyNetworkController.getInstance().sendContentNotification(
                contentNotification,
                new DonkyListener(){
                    @Override
                    public void success() {
                        states[move.getPosition()] = State.PLAYER1;
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), "success move", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(DonkyException e, Map<String, String> map) {
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void initStates(){
        states = new State[9];
        for (int i = 0; i < states.length; i++) {
            states[i] = State.EMPTY;
        }
    }

}
