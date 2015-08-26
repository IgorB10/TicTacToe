package com.donky.tictactoe.tictactoe;

public class Move {

    enum State{
        EMPTY,
        X,
        O
    }

    private String move;
    private String type;
    private State state;

    public Move(){
        this("", "", State.EMPTY);
    }

    public Move(String move, String type, State state) {
        this.move = move;
        this.type = type;
        this.state = state;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
