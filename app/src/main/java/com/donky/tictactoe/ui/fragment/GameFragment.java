package com.donky.tictactoe.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.donky.tictactoe.R;
import com.donky.tictactoe.model.Move;
import com.donky.tictactoe.tictactoe.GameMoves;
import com.donky.tictactoe.tictactoe.GameSession;
import com.donky.tictactoe.ui.view.GameView;

import java.util.Random;

import butterknife.Bind;

public class GameFragment extends BaseFragment {

    /** Start player. Must be 1 or 2. Default is 1. */
    public static final String EXTRA_START_PLAYER =
            "com.example.android.tictactoe.library.GameActivity.EXTRA_START_PLAYER";

    private static final int MSG_COMPUTER_TURN = 1;
    private static final long COMPUTER_DELAY_MS = 500;

    private Handler mHandler = new Handler(new MyHandlerCallback());
    private Random mRnd = new Random();

    private GameSession mGameSession;

    @Bind(R.id.game_view) GameView mGameView;
    @Bind(R.id.info_turn) TextView mInfoView;
    @Bind(R.id.next_turn) Button mButtonNext;

    public interface OnGameSession{
        GameSession currentSession();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof OnGameSession)
//            mGameSession = ((OnGameSession) activity).currentSession();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGameView.setFocusable(true);
        mGameView.setFocusableInTouchMode(true);
        mGameView.setCellListener(new MyCellListener());
        mGameView.setStates(mGameSession.getStates());
        mGameSession.setGameMoves(new GameMoves() {
            @Override
            public void move(Move move) {

            }

            @Override
            public void receive(Move move) {
                mGameView.update();
                selectTurn(GameView.State.PLAYER2);
                finishTurn();
            }
        });
        mButtonNext.setOnClickListener(new MyButtonListener());
    }

    @Override
    public void onResume() {
        super.onResume();

        GameView.State player = mGameView.getCurrentPlayer();
        if (player == GameView.State.UNKNOWN) {
            player = GameView.State.fromInt(getActivity().getIntent().getIntExtra(EXTRA_START_PLAYER, 1));
            if (!checkGameFinished(player)) {
                selectTurn(player);
            }
        }
        if (player == GameView.State.PLAYER2) {
            mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
        }
        if (player == GameView.State.WIN) {
            setWinState(mGameView.getWinner());
        }
    }

    public void setGameSession(GameSession session){
        mGameSession = session;
    }

    private GameView.State selectTurn(GameView.State player) {
        mGameView.setCurrentPlayer(player);
        mButtonNext.setEnabled(false);

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
            GameView.State player = mGameView.getCurrentPlayer();
            if (player == GameView.State.WIN) {
               getActivity().finish();
            } else if (player == GameView.State.PLAYER1) {
                int cell = mGameView.getSelection();
                if (cell >= 0) {
                    mGameView.setCell(cell, player);
                    mGameSession.sendMove(new Move(cell));
                    finishTurn();
                }
            }
        }
    }

    private class MyButtonListener implements View.OnClickListener {

        public void onClick(View v) {
        }
    }

    private class MyHandlerCallback implements Handler.Callback {
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_COMPUTER_TURN) {

                // Pick a non-used cell at random. That's about all the AI you need for this game.
                GameView.State[] data = mGameView.getDataStates();
                int used = 0;
                while (used != 0x1F) {
                    int index = mRnd.nextInt(9);
                    if (((used >> index) & 1) == 0) {
                        used |= 1 << index;
                        if (data[index] == GameView.State.EMPTY) {
                            mGameView.setCell(index, mGameView.getCurrentPlayer());
                            break;
                        }
                    }
                }

//                finishTurn();
                return true;
            }
            return false;
        }
    }

    private GameView.State getOtherPlayer(GameView.State player) {
        return player == GameView.State.PLAYER1 ? GameView.State.PLAYER2 : GameView.State.PLAYER1;
    }

    private void finishTurn() {
        GameView.State player = mGameView.getCurrentPlayer();
        if (!checkGameFinished(player)) {
            player = selectTurn(getOtherPlayer(player));
//            if (player == GameView.State.PLAYER2) {
//                mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
//            }
        }
    }

    public boolean checkGameFinished(GameView.State player) {
        GameView.State[] data = mGameView.getDataStates();
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

        mGameView.setCurrentPlayer(GameView.State.WIN);
        mGameView.setWinner(player);
        mGameView.setEnabled(false);
        mGameView.setFinished(col, row, diagonal);

        setWinState(player);
    }

    private void setWinState(GameView.State player) {
        mButtonNext.setEnabled(true);
        mButtonNext.setText("Back");

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
