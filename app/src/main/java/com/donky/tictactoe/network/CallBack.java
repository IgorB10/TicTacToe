package com.donky.tictactoe.network;

public interface CallBack<T> {

    void success(T response);

    void error(String message);
}
