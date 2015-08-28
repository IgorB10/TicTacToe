package com.donky.tictactoe;

import android.app.Application;

import com.donky.tictactoe.model.User;
import com.donky.tictactoe.network.CallBack;
import com.donky.tictactoe.utill.Constants;
import com.donky.tictactoe.utill.PreferencesManager;

import net.donky.core.DonkyCore;
import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;
import net.donky.core.ModuleDefinition;
import net.donky.core.Subscription;
import net.donky.core.account.DeviceDetails;
import net.donky.core.account.DonkyAccountController;
import net.donky.core.account.UserDetails;
import net.donky.core.network.ServerNotification;

import java.util.Map;

public class AppTicTakToe extends Application {

    private final String KEY = "xbtdB9hdea4mJ5AKbKyGV7QA+ZtodIcG18zECr62ZFKjgHjNbPMR9rpUPCfpbYKjNS1FL7OAncGdnee3zw";

    private static AppTicTakToe sAppTicTakToe;
    private PreferencesManager sPreferencesManager;
    private DeviceDetails deviceDetails = new DeviceDetails("my Nexus", "Nexus", null);


    @Override
    public void onCreate() {
        super.onCreate();
        sAppTicTakToe = this;
        sPreferencesManager = new PreferencesManager(this);
        String userId = sPreferencesManager.getUserId();
        String displayName = sPreferencesManager.getDisplayName();
        UserDetails userDetails = new UserDetails();
        if (userId != null && displayName != null)
            userDetails.setUserId(userId).setUserDisplayName(displayName);
        ModuleDefinition moduleDefinition = new ModuleDefinition("TicTakToe Game", "1.0.0.0");
        Subscription<ServerNotification> subscription =
                new Subscription<>(Constants.INVITE, InviteNotificationManager.getInstance());
        DonkyCore.subscribeToContentNotifications(moduleDefinition, subscription);
        DonkyCore.initialiseDonkySDK(this,
                KEY,
                userDetails,
                deviceDetails,
                "1.0.0.0",
                new DonkyListener() {
                    @Override
                    public void success() {
                    }

                    @Override
                    public void error(DonkyException e, Map<String, String> map) {
                    }
                }
        );
    }

    public static AppTicTakToe getsAppTicTakToe() {
        return sAppTicTakToe;
    }

    public PreferencesManager getPreferencesManager() {
        return sPreferencesManager;
    }

    public void updateUserDetails(User user, final CallBack callBack) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(user.getUserId()).setUserDisplayName(user.getDisplayName());
        DonkyAccountController.getInstance().updateRegistrationDetails(userDetails, deviceDetails, new DonkyListener(){
            @Override
            public void success() {
                callBack.success(null);
            }

            @Override
            public void error(DonkyException e, Map<String, String> map) {
                if (map != null) {
                    callBack.error(map.get("id"));
                }else if (e != null){
                    callBack.error(e.getMessage());
                }
            }
        });
    }

}
