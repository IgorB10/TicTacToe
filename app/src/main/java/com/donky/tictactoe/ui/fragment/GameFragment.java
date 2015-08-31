package com.donky.tictactoe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donky.tictactoe.R;
import com.donky.tictactoe.model.GameSessionController;
import com.donky.tictactoe.model.Move;
import com.donky.tictactoe.tictactoe.Game;
import com.donky.tictactoe.tictactoe.GameMoves;
import com.donky.tictactoe.ui.activity.GamesActivity;
import com.donky.tictactoe.ui.view.GameView;

import butterknife.Bind;

public class GameFragment extends BaseFragment {

    private GameSessionController mGameSessionController;

    @Bind(R.id.game_view) Game mGameView;
    @Bind(R.id.info_turn) TextView mInfoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = getArguments().getInt(GamesActivity.EXTRA_SELECTED_SESION);
        //FIXME
        mGameSessionController = ((GamesActivity)getActivity()).mGameManager.getGameSession(position);
        mGameView.onStartGame(mGameSessionController.getStates());
        if (mGameSessionController.getLastMovedPlayer() == GameView.State.EMPTY)
        selectTurn(mGameSessionController.isMyFirstMove() ? GameView.State.PLAYER1
                : GameView.State.PLAYER2);
        else
            selectTurn(mGameSessionController.getLastMovedPlayer());
        mGameView.setOnCellSelectedListener(new MyCellListener());
        mGameSessionController.setGameMoves(new GameMoves() {
            @Override
            public void move(Move move) {

            }

            @Override
            public void receive(Move move) {
                mGameView.onMoveMade(move);
                selectTurn(GameView.State.PLAYER2);
                finishTurn();
            }
        });
    }

    private GameView.State selectTurn(GameView.State player) {
        mGameView.setCurrentPlayer(player);
        if (player == GameView.State.PLAYER1) {
            mInfoView.setText(R.string.player1_turn);
            mGameView.setEnabled(true);

        } else if (player == GameView.State.PLAYER2) {
            mInfoView.setText(R.string.player2_turn);
            mGameView.setEnabled(false);
        }

        return player;
    }

    private class MyCellListener implements GameView.OnCellSelectedListener {
        public void onCellSelected() {
            GameView.State player = mGameView.getCurrentState();
            if (player == GameView.State.WIN) {
               getActivity().finish();
            } else if (player == GameView.State.PLAYER1) {
                int cell = mGameView.getSelection();
                if (cell >= 0) {
                    mGameView.setCell(cell, player);
                    mGameSessionController.sendMove(cell);
                    finishTurn();
                }
            }
        }
    }

    private GameView.State getOtherPlayer(GameView.State player) {
        return player == GameView.State.PLAYER1 ? GameView.State.PLAYER2 : GameView.State.PLAYER1;
    }

    private void finishTurn() {
        GameView.State player = mGameView.getCurrentState();
        if (!checkGameFinished(player)) {
            player = selectTurn(getOtherPlayer(player));
        }
    }

    public boolean checkGameFinished(GameView.State player) {
        GameView.State[] data = mGameSessionController.getStates();
        boolean full = true;

        int col = -1;
        int row = -1;
        int diag = -1;

        // check rows
        for (int j = 0, k = 0; j < 3; j++, k += 3) {
            if (data[k] != GameView.State.EMPTY && data[k] == data[k+1] && data[k] == data[k+2]) {
                row = j;
            }
            if (full && (data[k] == GameView.State.EMPTY ||
                    data[k+1] == GameView.State.EMPTY ||
                    data[k+2] == GameView.State.EMPTY)) {
                full = false;
            }
        }

        // check columns
        for (int i = 0; i < 3; i++) {
            if (data[i] != GameView.State.EMPTY && data[i] == data[i+3] && data[i] == data[i+6]) {
                col = i;
            }
        }

        // check diagonals
        if (data[0] != GameView.State.EMPTY && data[0] == data[1+3] && data[0] == data[2+6]) {
            diag = 0;
        } else  if (data[2] != GameView.State.EMPTY && data[2] == data[1+3] && data[2] == data[0+6]) {
            diag = 1;
        }

        if (col != -1 || row != -1 || diag != -1) {
            setFinished(player, col, row, diag);
            return true;
        }

        // if we get here, there's no winner but the board is full.
        if (full) {
            setFinished(GameView.State.EMPTY, -1, -1, -1);
            return true;
        }
        return false;
    }

    private void setFinished(GameView.State player, int col, int row, int diagonal) {

        mGameSessionController.setGameOver(player);

        mGameView.setCurrentPlayer(GameView.State.WIN);
        mGameView.setWinner(player);
        mGameView.setEnabled(false);
        mGameView.setFinished(col, row, diagonal);

        setWinState(player);
    }

    private void setWinState(GameView.State player) {
        String text;
        if (player == GameView.State.EMPTY) {
            text = getString(R.string.tie);
        } else if (player == GameView.State.PLAYER1) {
            text = getString(R.string.player1_win);
        } else {
            text = getString(R.string.player2_win);
        }
        mInfoView.setText(text);
    }
}
