package com.donky.tictactoe.tictactoe;

public class Move {

    private String move;
    private String type;

    public Move(String move, String type) {
        this.move = move;
        this.type = type;
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
}
