package com.donky.tictactoe.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.donky.tictactoe.R;
import com.donky.tictactoe.model.Invite;

public class AddGameDialog extends DialogFragment {

    private EditText mEditTextUserId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_game_dialog, null);
        mEditTextUserId = (EditText)view.findViewById(R.id.et_user_id);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.send_invite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() instanceof OnUserInviteListener)
                            ((OnUserInviteListener)getActivity())
                                    .sendInvite(mEditTextUserId.getEditableText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }

    public interface OnUserInviteListener{
        void sendInvite(String userId);
        void acceptInvite(Invite invite);
        void declineInvite(Invite invite);
    }
}
