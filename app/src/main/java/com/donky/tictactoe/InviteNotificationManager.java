package com.donky.tictactoe;

import com.donky.tictactoe.model.Invite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.donky.core.NotificationListener;
import net.donky.core.network.ServerNotification;

import java.util.LinkedList;

public class InviteNotificationManager implements NotificationListener<ServerNotification> {

    private LinkedList<OnInviteListener> mInviteListeners;

    private InviteNotificationManager(){
        mInviteListeners = new LinkedList<>();
    }

    public interface OnInviteListener {
        void notifyObservers(Invite object);
    }

    private static class SingletonHolder {
        private static final InviteNotificationManager INSTANCE = new InviteNotificationManager();
    }

    public static InviteNotificationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addListener(OnInviteListener listener) {
        mInviteListeners.add(listener);
    }

    public void removeListener(OnInviteListener listener) {
        mInviteListeners.remove(listener);
    }

    @Override
    public void onNotification(ServerNotification serverNotification) {
        JsonObject data = serverNotification.getData();
        Gson gson = new GsonBuilder().create();
        Invite invite = gson.fromJson(data.get("customData"), Invite.class);
        for (OnInviteListener listener : mInviteListeners)
            listener.notifyObservers(invite);
    }
}
