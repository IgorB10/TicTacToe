package com.donky.tictactoe.model;

public class Move {

    private int position;

    public Move(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
