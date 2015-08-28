package com.donky.tictactoe.tictactoe;

import com.donky.tictactoe.model.Move;
import com.donky.tictactoe.ui.view.GameView;

public interface Game {

    void onStartGame(GameView.State[] states);

    GameSession getSession();

    void onPauseGame();

    void onStopGame();

    void onFinishGame();

    void onMoveMade(Move move);

    void setOnCellSelectedListener(GameView.OnCellSelectedListener listener);

    GameView.State getCurrentState();

    void setCurrentPlayer(GameView.State player);

    void setEnabled(boolean isEnable);

    int getSelection();

    void setCell(int cellIndex, GameView.State value);

    void setWinner(GameView.State state);

    void setFinished(int col, int row, int diagonal);

}
