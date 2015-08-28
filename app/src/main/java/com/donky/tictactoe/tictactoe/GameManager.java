package com.donky.tictactoe.tictactoe;

import android.widget.Toast;

import com.donky.tictactoe.AppTicTakToe;
import com.donky.tictactoe.InviteNotificationManager;
import com.donky.tictactoe.model.Invite;
import com.donky.tictactoe.utill.Constants;
import com.google.gson.Gson;

import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;
import net.donky.core.network.DonkyNetworkController;
import net.donky.core.network.content.ContentNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class GameManager implements InviteNotificationManager.OnInviteListener {

    private ArrayList<GameSession> mGameSessions;
    private Game mGame;
    private ArrayList<Invite> mSendInvited;
    private Random random = new Random();

    public GameManager(Game game) {
        mGame = game;
        mSendInvited = new ArrayList<>();
        mGameSessions = new ArrayList<>();
    }

    @Override
    public void notifyObservers(Invite invite) {
        if (mSendInvited.contains(invite)) {
            createGameSession(invite);
        }else if (Constants.PROPOSE.equals(invite.getState())){
            mGame.onInviteReceived(invite);
        }else if (Constants.DECLINE.equals(invite.getState())){
            mSendInvited.remove(invite);
        }
    }

    public void addListeners(){
        InviteNotificationManager.getInstance().addListener(this);
    }

    public void removeListeners(){
        InviteNotificationManager.getInstance().removeListener(this);
    }

    private void createGameSession(Invite invite){
        GameSession gameSession = new GameSession(invite);
        mGameSessions.add(gameSession);
        mSendInvited.remove(invite);
        mGame.onCreateGame(gameSession);
    }

    public void acceptInvite(Invite invite){
        sendInvite(invite, false);
        createGameSession(invite);
    }

    public void sendInvite(String toUserId){
        final Invite invite = new Invite(AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId(),
                toUserId, random.nextInt(Integer.MAX_VALUE), Constants.PROPOSE,
                random.nextBoolean() ?  AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId()  : toUserId);
        sendInvite(invite, true);
    }

    private void sendInvite(final Invite invite, final boolean isSaveInvite){
        String jsonString = new Gson().toJson(invite);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        }catch (JSONException e){
            e.printStackTrace();
        }
        ContentNotification contentNotification =
                new ContentNotification(invite.getOpponentUserId(), Constants.INVITE, jsonObject);

        DonkyNetworkController.getInstance().sendContentNotification(
                contentNotification,
                new DonkyListener() {
                    @Override
                    public void success() {
                        if (isSaveInvite)
                            mSendInvited.add(invite);
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), "success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(DonkyException e, Map<String, String> map) {
                        Toast.makeText(AppTicTakToe.getsAppTicTakToe(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void declineInvite(Invite invite){
        mSendInvited.remove(invite);
    }

    public GameSession getGameSession(int position){
        return mGameSessions.get(position);
    }

    public ArrayList<GameSession> getGameSessions() {
        return mGameSessions;
    }


    public interface Game{

        void onInviteSend();

        void onCreateGame(GameSession gameSession);

        void onInviteReceived(Invite invite);

        void onFinishGame(GameSession gameSession);
    }

}
