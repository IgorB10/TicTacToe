package com.donky.tictactoe.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.donky.tictactoe.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TicTakToeFragment extends BaseFragment {

    private int height;

    @Bind(R.id.gridview)
    GridView mTikTakToeGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ticktaktoe_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels / 3;
        ArrayList<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        strings.add("7");
        strings.add("8");
        strings.add("9");
        mTikTakToeGridView.setAdapter(new TikTakToeAdapter(getActivity(), strings));
    }

    private class TikTakToeAdapter extends ArrayAdapter<String> {

        public TikTakToeAdapter(Context context, ArrayList<String> arrayList) {
            super(context, 0, arrayList);
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
            final String user = getItem(position);
            viewHolder.content.setText(user);
            return convertView;
        }
    }

    protected class ViewHolder {

        @Bind(R.id.content)
        TextView content;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            content.setMinimumHeight(height);
        }
    }
}
