package com.winax.musicplayer.models;




public class Favlist {
    public final long id;
    public final String name;
    public final int songCount;

    public Favlist() {
        this.id = -1;
        this.name = "";
        this.songCount = -1;
    }

    public Favlist(long _id, String _name, int _songCount) {
        this.id = _id;
        this.name = _name;
        this.songCount = _songCount;
    }
}

