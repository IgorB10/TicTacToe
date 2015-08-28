package com.donky.tictactoe.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.donky.tictactoe.AppTicTakToe;

public class Invite implements Parcelable {

    private String toUserId;
    private String fromUserId;
    private int gameId;
    private String state;
    private String firstMove;

    public Invite(String toUserId, String fromUserId, int gameId, String state, String firstMove) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.gameId = gameId;
        this.state = state;
        this.firstMove = firstMove;
    }

    private Invite(Parcel in) {
        fromUserId = in.readString();
        toUserId = in.readString();
        gameId = in.readInt();
        state = in.readString();
        firstMove = in.readString();
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getState() {
        return state;
    }

    public boolean isMyFirstMove(){
        return AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId().equals(firstMove);
    }

    public String getOpponentUserId(){
        return AppTicTakToe.getsAppTicTakToe().getPreferencesManager().getUserId().equals(fromUserId)
                ? toUserId : fromUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invite invite = (Invite) o;

        if (gameId != invite.gameId) return false;
        if (toUserId != null ? !toUserId.equals(invite.toUserId) : invite.toUserId != null)
            return false;
        if (fromUserId != null ? !fromUserId.equals(invite.fromUserId) : invite.fromUserId != null)
            return false;
        if (state != null ? !state.equals(invite.state) : invite.state != null) return false;
        return !(firstMove != null ? !firstMove.equals(invite.firstMove) : invite.firstMove != null);

    }

    @Override
    public int hashCode() {
        int result = toUserId != null ? toUserId.hashCode() : 0;
        result = 31 * result + (fromUserId != null ? fromUserId.hashCode() : 0);
        result = 31 * result + gameId;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (firstMove != null ? firstMove.hashCode() : 0);
        return result;
    }

    public static final Parcelable.Creator<Invite> CREATOR
            = new Parcelable.Creator<Invite>() {
        public Invite createFromParcel(Parcel in) {
            return new Invite(in);
        }

        public Invite[] newArray(int size) {
            return new Invite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromUserId);
        dest.writeString(toUserId);
        dest.writeInt(gameId);
        dest.writeString(state);
        dest.writeString(firstMove);
    }
}