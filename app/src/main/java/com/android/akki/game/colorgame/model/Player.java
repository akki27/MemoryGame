package com.android.akki.game.colorgame.model;

import java.io.Serializable;

/**
 * Created by SadyAkki on 5/18/2017.
 */

public class Player implements Serializable {

    String name;
    String score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
