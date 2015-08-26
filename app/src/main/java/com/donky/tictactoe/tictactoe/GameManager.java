package com.donky.tictactoe.tictactoe;

import android.widget.Toast;

import com.donky.tictactoe.AppTicTakToe;

import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;
import net.donky.core.network.DonkyNetworkController;
import net.donky.core.network.content.ContentNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class GameManager {

    private ArrayList<GameSession> mGameSessions;
    private Game mGame;

    public GameManager(Game game){
        mGame = game;
    }

    public interface Game{

        void onCreateGame(GameSession gameSession);

        void onFinishGame(GameSession gameSession);
    }

    public GameSession getGameSession(int position){
        return mGameSessions.get(position);
    }

    public void sendInvite(String userId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("kingMove", "A1 - B4");
        }catch (JSONException e){
            e.printStackTrace();
        }

        ContentNotification contentNotification =
                new ContentNotification("test_device_1", "chessMove", jsonObject);

        DonkyNetworkController.getInstance().sendContentNotification(
                contentNotification,
                new DonkyListener(){
                    @Override
                    public void success() {
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), "success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(DonkyException e, Map<String, String> map) {
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
