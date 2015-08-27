package com.donky.tictactoe.tictactoe;

import com.donky.tictactoe.model.Move;

public interface GameMoves {

    void move(Move move);

    void receive(Move move);
}
