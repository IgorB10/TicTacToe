package com.donky.tictactoe.tictactoe;

import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Map;

public class GameSession implements GameMoves, NotificationListener<ServerNotification> {

    private final String mId;
    private ArrayList<Move> moves;

    public GameSession(String id){
        mId = id;
        moves = defaultMoves();
        Subscription<ServerNotification> subscription =
                new Subscription<>("chessMove", this);
        DonkyCore.subscribeToContentNotifications(
                new ModuleDefinition("Chess Game", "1.0.0.0"), subscription
        );
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    @Override
    public void move(Move move) {

    }

    @Override
    public void receive(Move move) {

    }

    @Override
    public void onNotification(ServerNotification serverNotification) {

    }

    private void sendMove(Move move){
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
//                        Toast.makeText(GamesListActivity.this, "success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(DonkyException e, Map<String, String> map) {
//                        Toast.makeText(GamesListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
            });
    }

    public ArrayList<Move> defaultMoves(){
        ArrayList<Move> moves = new ArrayList<>(9);
        moves.add(new Move());
        moves.add(new Move());
        moves.add(new Move());
        moves.add(new Move());
        moves.add(new Move());
        moves.add(new Move());
        moves.add(new Move());
        moves.add(new Move());
        moves.add(new Move());
        return moves;
    }

}
