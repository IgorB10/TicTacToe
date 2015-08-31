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

public class GameSession {

    private Invite mInvite;
    private State[] states;
    private State lastMove;

    public GameSession(Invite invite){
        mInvite = invite;
        initStates();

    }

    public State getLastMove() {
        return lastMove;
    }

    public void setLastMove(State lastMove) {
        this.lastMove = lastMove;
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



    public void initStates(){
        states = new State[9];
        for (int i = 0; i < states.length; i++) {
            states[i] = State.EMPTY;
        }
    }

}
