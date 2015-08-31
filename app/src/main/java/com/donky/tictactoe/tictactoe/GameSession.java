package com.donky.tictactoe.tictactoe;

import com.donky.tictactoe.model.Invite;
import com.donky.tictactoe.ui.view.GameView.State;

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
