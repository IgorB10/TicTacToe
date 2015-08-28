package com.donky.tictactoe.tictactoe;

import android.widget.Toast;

import java.util.Map;

import com.donky.tictactoe.AppTicTakToe;
import com.donky.tictactoe.model.Invite;
import com.donky.tictactoe.model.Move;
import com.donky.tictactoe.ui.fragment.GameFragment;
import com.donky.tictactoe.ui.view.GameView.State;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.donky.core.DonkyCore;
import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;
import net.donky.core.ModuleDefinition;
import net.donky.core.NotificationListener;
import net.donky.core.Subscription;
import net.donky.core.network.DonkyNetworkController;
import net.donky.core.network.ServerNotification;
import net.donky.core.network.content.ContentNotification;

import org.json.JSONException;
import org.json.JSONObject;

public class GameSession implements NotificationListener<ServerNotification> {

    private Invite mInvite;
    private State[] states;
    private State lastMove;


    private GameFragment gameFragment;
    private GameMoves mGameMoves;

    public GameSession(Invite invite){
        mInvite = invite;
        initStates();
        ModuleDefinition moduleDefinition = new ModuleDefinition("TicTakToe Game", "1.0.0.0");
        Subscription subscription = new Subscription<>(AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId() + "_" + mInvite.getGameId(), this);
        DonkyCore.subscribeToContentNotifications(moduleDefinition, subscription);
    }

    @Override
    public void onNotification(ServerNotification serverNotification) {

        JsonObject data = serverNotification.getData();
        String type = data.get("customType").getAsString();
        if ((AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId() + "_" + mInvite.getGameId()).equals(type)) {
            Gson gson = new GsonBuilder().create();
            Move move = gson.fromJson(data.get("customData"), Move.class);
            states[move.getPosition()] = State.PLAYER2;
            if (mGameMoves != null)
                mGameMoves.receive(move);
        }
    }

    public State getLastMove() {
        return lastMove;
    }

    public void setLastMove(State lastMove) {
        this.lastMove = lastMove;
    }

    public void setGameFragment(GameFragment gameFragment) {
        this.gameFragment = gameFragment;
    }

    public void setGameMoves(GameMoves mGameMoves) {
        this.mGameMoves = mGameMoves;
    }

//    @Override
//    public void move(Move move) {
//        sendMove(move);
//    }
//
//    @Override
//    public void receive(Move move) {
//        states[move.getPosition()] = State.PLAYER2;
//        gameFragment.update();
//    }

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

    public void sendMove(final Move move){
        String jsonString = new Gson().toJson(move);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        }catch (JSONException e){
            e.printStackTrace();
        }
        ContentNotification contentNotification =
                new ContentNotification(mInvite.getOpponentUserId(), mInvite.getOpponentUserId()+ "_" + mInvite.getGameId(), jsonObject);

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
