package com.winax.musicplayer.rx;


import io.reactivex.functions.Action;

public interface UnsafeAction extends Action {

    @Override
    void run();
}