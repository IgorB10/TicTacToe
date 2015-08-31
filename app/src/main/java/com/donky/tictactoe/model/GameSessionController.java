package com.donky.tictactoe.model;

import android.widget.Toast;

import com.donky.tictactoe.AppTicTakToe;
import com.donky.tictactoe.tictactoe.GameMovesListener;
import com.donky.tictactoe.tictactoe.GameSession;
import com.donky.tictactoe.ui.view.GameView;
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

import java.util.Map;

public class GameSessionController implements NotificationListener<ServerNotification> {

    private GameSession mGameSession;
    private GameState mGameState;
    private GameMovesListener mGameMovesListener;

    private GameView.State winPlayer = GameView.State.EMPTY;

    private GameView.State lastMovedPlayer = GameView.State.EMPTY;

    public enum GameState{
        STARTING,
        PLAYING,
        PAUSE,
        FINISH
    }

    public GameSessionController(Invite invite){
        mGameSession = new GameSession(invite);
        mGameState = GameState.STARTING;
        ModuleDefinition moduleDefinition = new ModuleDefinition("TicTakToe Game", "1.0.0.0");
        Subscription subscription = new Subscription<>(AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId() + "_" + invite.getGameId(), this);
        DonkyCore.subscribeToContentNotifications(moduleDefinition, subscription);
    }

    public void setGameMovesListener(GameMovesListener mGameMovesListener) {
        this.mGameMovesListener = mGameMovesListener;
    }

    public void removeGameMovesListener(){
        this.mGameMovesListener = null;
    }

    public void setGameState(GameState gameState) {
        this.mGameState = gameState;
    }

    public GameState getGameState() {
        return mGameState;
    }

    public GameView.State[] getStates() {
        return mGameSession.getStates();
    }


    public String getOpponentName(){
        return mGameSession.getmInvite().getOpponentUserId();
    }

    public boolean isMyFirstMove(){
        return AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId().equals(mGameSession.getmInvite().getFirstMove());
    }

    public GameSession getGameSession() {
        return mGameSession;
    }

    public void setGameOver(GameView.State player){
        winPlayer = player;
        mGameState = GameState.FINISH;
    }

    public GameView.State getCurrentMove(){
        return lastMovedPlayer == GameView.State.PLAYER1
                ? GameView.State.PLAYER2 : GameView.State.PLAYER1;
    }

    private void setState(int position, GameView.State player){
        mGameSession.getStates()[position] = player;
    }

    private GameView.State getState(int position){
        return mGameSession.getStates()[position];
    }

    public GameView.State getWinPlayer() {
        return winPlayer;
    }

    public GameView.State getLastMovedPlayer() {
        return lastMovedPlayer;
    }

    @Override
    public void onNotification(ServerNotification serverNotification) {
        JsonObject data = serverNotification.getData();
        String type = data.get("customType").getAsString();
        if ((AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId() + "_" + mGameSession.getmInvite().getGameId()).equals(type)) {
            Gson gson = new GsonBuilder().create();
            Move move = gson.fromJson(data.get("customData"), Move.class);
            lastMovedPlayer = GameView.State.PLAYER2;
            if (mGameMovesListener != null && getState(move.getPosition()) == GameView.State.EMPTY) {
                mGameMovesListener.receive(move);
            }else if (getState(move.getPosition()) == GameView.State.EMPTY){
                setState(move.getPosition(), GameView.State.PLAYER2);
            }
        }
    }

    public void sendMove(int position){
        Move move = new Move(position);
        if (mGameMovesListener != null)
            mGameMovesListener.move(move);
        sendMove(move);
    }

    private void sendMove(final Move move){


        String jsonString = new Gson().toJson(move);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        }catch (JSONException e){
            e.printStackTrace();
        }
        ContentNotification contentNotification =
                new ContentNotification(mGameSession.getmInvite().getOpponentUserId(),
                        mGameSession.getmInvite().getOpponentUserId() + "_"
                                + mGameSession.getmInvite().getGameId(), jsonObject);

        DonkyNetworkController.getInstance().sendContentNotification(
                contentNotification,
                new DonkyListener(){
                    @Override
                    public void success() {
                        lastMovedPlayer = GameView.State.PLAYER1;
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), "success move", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(DonkyException e, Map<String, String> map) {
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSessionController that = (GameSessionController) o;

        return !(mGameSession != null ? !mGameSession.equals(that.mGameSession) : that.mGameSession != null);

    }

    @Override
    public int hashCode() {
        return mGameSession != null ? mGameSession.hashCode() : 0;
    }
}
