package com.donky.tictactoe.tictactoe;

public class GameController {

    interface GameState{

        void move(int cell);

        void finish();
    }
}
