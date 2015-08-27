package com.donky.tictactoe;

import com.donky.tictactoe.model.Invite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.donky.core.NotificationListener;
import net.donky.core.network.ServerNotification;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationManager implements NotificationListener<ServerNotification> {

    private HashMap<String, ArrayList<OnNotificationListener>> mNotificationMap;

    private NotificationManager(){
        mNotificationMap = new HashMap<>();
    }

    public interface OnNotificationListener<T> {
        void notifyObservers(T object);
    }

    private static class SingletonHolder {
        private static final NotificationManager INSTANCE = new NotificationManager();
    }

    public static NotificationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public<T> void addListener(String tag, OnNotificationListener<T> listener) {
        if (mNotificationMap.containsKey(tag))
            mNotificationMap.get(tag).add(listener);
        else {
            ArrayList<OnNotificationListener> list = new ArrayList<>();
            list.add(listener);
            mNotificationMap.put(tag, list);
        }
    }

    public void removeListener(String tag, OnNotificationListener listener) {
        if (mNotificationMap.containsKey(tag))
            mNotificationMap.get(tag).remove(listener);
    }



    @Override
    public void onNotification(ServerNotification serverNotification) {
        JsonObject data = serverNotification.getData();
        String type = data.get("customType").getAsString();
        Gson gson = new GsonBuilder().create();
        Invite invite = gson.fromJson(data.get("customData"), Invite.class);
        for (OnNotificationListener listener : mNotificationMap.get(type))
            listener.notifyObservers(invite);
    }
}
