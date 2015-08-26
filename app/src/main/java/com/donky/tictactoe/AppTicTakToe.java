package com.donky.tictactoe;

import android.app.Application;
import android.provider.SyncStateContract;
import android.widget.Toast;

import com.donky.tictactoe.controllers.RegistrationController;
import com.donky.tictactoe.model.User;
import com.donky.tictactoe.network.CallBack;
import com.donky.tictactoe.ui.AppDonkyListener;

import net.donky.core.DonkyCore;
import net.donky.core.DonkyException;
import net.donky.core.DonkyListener;
import net.donky.core.ModuleDefinition;
import net.donky.core.NotificationListener;
import net.donky.core.Subscription;
import net.donky.core.account.DeviceDetails;
import net.donky.core.account.UserDetails;
import net.donky.core.network.ServerNotification;

import java.util.Map;

public class AppTicTakToe extends Application {

    private final String KEY = "xbtdB9hdea4mJ5AKbKyGV7QA+ZtodIcG18zECr62ZFKjgHjNbPMR9rpUPCfpbYKjNS1FL7OAncGdnee3zw";

    private static AppTicTakToe sAppTicTakToe;
    private RegistrationController mRegistrationController;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppTicTakToe = this;
        mRegistrationController = new RegistrationController();
    }

    public static AppTicTakToe getsAppTicTakToe() {
        return sAppTicTakToe;
    }

    public void initDonkyModel(User user, final CallBack callBack) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(user.getUserId()).setUserDisplayName(user.getDisplayName());
        DeviceDetails deviceDetails = new DeviceDetails("my Nexus", "Nexus", null);
        DonkyCore.initialiseDonkySDK(this,
                KEY,
                userDetails,
                deviceDetails,
                "1.0.0.0",
                new DonkyListener() {
                    @Override
                    public void success() {
                        callBack.success(null);
                    }

                    @Override
                    public void error(DonkyException e, Map<String, String> map) {
                        callBack.error(e.getMessage());
                    }
                }
        );

    NotificationListener<ServerNotification> listener =
            new NotificationListener<ServerNotification>() {

                @Override
                public void onNotification(ServerNotification notification) {
                    Toast.makeText(AppTicTakToe.this, notification.getId() + " " + notification.getData().toString(), Toast.LENGTH_LONG).show();

                      /* Code to handle the notification(s) as they are received and
                      processed */

                }

            };

    // Create Subscription to receive the 'chessMove' custom type.
    Subscription<ServerNotification> subscription =
            new Subscription<>("chessMove", listener);

    //Subscribe module definition and above subscription with the Donky Core
    DonkyCore.subscribeToContentNotifications(
            new ModuleDefinition("Chess Game", "1.0.0.0"), subscription
    );
    }

}
