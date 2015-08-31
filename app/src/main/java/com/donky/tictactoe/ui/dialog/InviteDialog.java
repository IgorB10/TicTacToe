package com.donky.tictactoe.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.donky.tictactoe.R;
import com.donky.tictactoe.model.Invite;

public class InviteDialog extends DialogFragment {

    private static String EXTRA_INVITE = "invite";

    private TextView mInvitedUser;
    private Invite mInvite;

    public static InviteDialog newInstance(Invite invite) {
        InviteDialog f = new InviteDialog();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_INVITE, invite);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInvite = getArguments().getParcelable(EXTRA_INVITE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.invite_dialog, null);
        mInvitedUser = (TextView)view.findViewById(R.id.invited_user_id);
        mInvitedUser.setText(mInvite.getOpponentUserId());
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() instanceof AddGameDialog.OnUserInviteListener)
                            ((AddGameDialog.OnUserInviteListener) getActivity())
                                    .acceptInvite(mInvite);
                    }
                })
                .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((AddGameDialog.OnUserInviteListener) getActivity())
                                .declineInvite(mInvite);
                    }
                })
                .create();
    }


}
