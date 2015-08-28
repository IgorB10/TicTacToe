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
import android.widget.TextView;

import com.donky.tictactoe.R;
import com.donky.tictactoe.tictactoe.GameSession;
import com.donky.tictactoe.ui.dialog.AddGameDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GamesListFragment extends BaseFragment {

    @Bind(R.id.lv_games)
    ListView mSessionGamesListView;

    private GamesListAdapter mGamesListAdapter;
    private OnSessionSelectedListener mSessionSelectedListener;
    private ArrayList<GameSession> mGameSessions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.games_listfragement, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                mSessionSelectedListener.sessionPosition(position);
            }
        });
    }

    public void setSession(ArrayList<GameSession> sessions){
        mGameSessions = sessions;
    }

    public void notifyAdapter(){
        mGamesListAdapter.notifyDataSetChanged();
    }

    public void setmSessionSelectedListener(OnSessionSelectedListener mSessionSelectedListener) {
        this.mSessionSelectedListener = mSessionSelectedListener;
    }

    private class GamesListAdapter extends ArrayAdapter<GameSession>{

        public GamesListAdapter(Context context, ArrayList<GameSession> sessions){
            super(context, 0, sessions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            final GameSession session = getItem(position);
            viewHolder.content.setText(session.getmInvite().getOpponentUserId());
            return convertView;
        }
    }

    protected class ViewHolder {

        @Bind(R.id.content)
        TextView content;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnSessionSelectedListener{
        void sessionPosition(int position);
    }


}
