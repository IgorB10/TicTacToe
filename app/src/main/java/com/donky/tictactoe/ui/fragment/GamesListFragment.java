package com.donky.tictactoe.ui.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.donky.tictactoe.R;
import com.donky.tictactoe.model.GameSessionController;
import com.donky.tictactoe.ui.dialog.AddGameDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GamesListFragment extends BaseFragment {

    @Bind(R.id.lv_games)
    ListView mSessionGamesListView;

    @Bind(android.R.id.empty)
    TextView emptyTextView;

    private GamesListAdapter mGamesListAdapter;
    private OnSessionSelectedListener mSessionSelectedListener;
    private ArrayList<GameSessionController> mGameSessions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.games_listfragement, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSessionGamesListView.setEmptyView(emptyTextView);
        initAdapter();
    }

    @OnClick(R.id.fab_add_game)
    public void addGame(){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("AddGameDialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            DialogFragment newFragment = new AddGameDialog();
            newFragment.show(ft, "AddGameDialog");
    }

    public void initAdapter(){
        mGamesListAdapter = new GamesListAdapter(getActivity(), mGameSessions);
        mSessionGamesListView.setAdapter(mGamesListAdapter);
        mSessionGamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameSessionController.GameState state = mGamesListAdapter.getItem(position).getGameState();
                if (state == GameSessionController.GameState.STARTING)
                    Toast.makeText(getActivity(), "Please wait, game will start in a minute", Toast.LENGTH_LONG).show();
                else if (state == GameSessionController.GameState.FINISH)
                    Toast.makeText(getActivity(), "This game is finish", Toast.LENGTH_LONG).show();
                else
                    mSessionSelectedListener.sessionPosition(position);
            }
        });
    }

    public void setSession(ArrayList<GameSessionController> sessions){
        mGameSessions = sessions;
    }

    public void notifyAdapter(){
        mGamesListAdapter.notifyDataSetChanged();
    }

    public void setSessionSelectedListener(OnSessionSelectedListener mSessionSelectedListener) {
        this.mSessionSelectedListener = mSessionSelectedListener;
    }

    private class GamesListAdapter extends ArrayAdapter<GameSessionController>{

        public GamesListAdapter(Context context, ArrayList<GameSessionController> sessions){
            super(context, 0, sessions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.game_session_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            final GameSessionController sessionController = getItem(position);
            if (sessionController.getGameState() == GameSessionController.GameState.STARTING){
                viewHolder.content.setText(getString(R.string.starting_game_with) + sessionController.getOpponentName());
                viewHolder.progressGame.setVisibility(View.VISIBLE);
            }else if (sessionController.getGameState() == GameSessionController.GameState.PLAYING){
                viewHolder.content.setText(getString(R.string.playing_with) + " " + sessionController.getOpponentName());
                viewHolder.progressGame.setVisibility(View.INVISIBLE);
            }else if (sessionController.getGameState() == GameSessionController.GameState.FINISH){
                viewHolder.content.setText(getString(R.string.finish_game));
                viewHolder.progressGame.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
    }

    protected class ViewHolder {

        @Bind(R.id.content)
        TextView content;

        @Bind(R.id.progress_game)
        ProgressBar progressGame;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnSessionSelectedListener{
        void sessionPosition(int position);
    }


}
