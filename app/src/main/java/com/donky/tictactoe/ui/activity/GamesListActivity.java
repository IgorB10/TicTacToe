package com.donky.tictactoe.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.donky.tictactoe.R;
import com.donky.tictactoe.tictactoe.GameManager;
import com.donky.tictactoe.tictactoe.GameSession;
import com.donky.tictactoe.ui.dialog.AddGameDialog;
import com.donky.tictactoe.ui.fragment.GamesListFragment;

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

public class GamesListActivity extends AppCompatActivity implements AddGameDialog.OnUserInviteListener{

    private GamesListFragment mGamesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_list_activity);
        mGamesListFragment = (GamesListFragment)getFragmentManager().findFragmentById(R.id.game_listfragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendInvite(String userId) {
       mGamesListFragment.sendInvite(userId);
    }
}
