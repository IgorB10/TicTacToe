package com.donky.tictactoe.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.donky.tictactoe.R;
import com.donky.tictactoe.model.Invite;
import com.donky.tictactoe.tictactoe.GameManager;
import com.donky.tictactoe.ui.dialog.AddGameDialog;
import com.donky.tictactoe.ui.dialog.InviteDialog;
import com.donky.tictactoe.ui.fragment.GameFragment;
import com.donky.tictactoe.ui.fragment.GamesListFragment;

public class GamesActivity extends Activity implements AddGameDialog.OnUserInviteListener,
                                                                GameManager.Game,
                                                                GamesListFragment.OnSessionSelectedListener {

    public static String EXTRA_SELECTED_SESION = "session";

    private GamesListFragment mGamesListFragment;
    public GameManager mGameManager;
    private GameFragment mGameFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_activity);
        if (savedInstanceState == null){
            mGamesListFragment = new GamesListFragment();
            mGamesListFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.content, mGamesListFragment).commit();
        }
        mGameManager = new GameManager(this);
        mGamesListFragment.setSession(mGameManager.getGameSessions());
        mGamesListFragment.setSessionSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameManager.addListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameManager.removeListeners();
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
        mGameManager.sendInvite(userId);
    }

    @Override
    public void acceptInvite(Invite invite) {
        mGameManager.acceptInvite(invite);
    }


    @Override
    public void declineInvite(Invite invite) {
        mGameManager.declineInvite(invite);
    }

    @Override
    public void onInviteSend() {
        mGamesListFragment.notifyAdapter();
    }

    @Override
    public void onCreateGame() {
        mGamesListFragment.notifyAdapter();
    }

    @Override
    public void onInviteReceived(Invite invite) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("InviteDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InviteDialog newFragment = InviteDialog.newInstance(invite);
        if (!getFragmentManager().isDestroyed())
            newFragment.show(ft, "InviteDialog");
    }

    @Override
    public void onFinishGame() {

    }

    @Override
    public void sessionPosition(int position) {
        if (mGameFragment == null)
            mGameFragment = new GameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SELECTED_SESION, position);
        mGameFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.content, mGameFragment, "GameFragment")
                .addToBackStack("mGamesListFragment_GameFragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag("GameFragment") != null) {
            getFragmentManager()
                    .popBackStack("mGamesListFragment_GameFragment",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }


}
