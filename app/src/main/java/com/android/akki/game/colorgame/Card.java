package com.android.akki.game.colorgame;

import android.widget.Button;

/**
 * Created by e01106 on 5/17/2017.
 */
public class Card{

    public int x;
    public int y;
    public Button button;

    public Card(Button button, int x,int y) {
        this.x = x;
        this.y=y;
        this.button=button;
    }


}
