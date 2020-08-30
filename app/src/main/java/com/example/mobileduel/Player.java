package com.example.mobileduel;

import com.google.android.gms.maps.model.LatLng;

public class Player {

    //true state = alive, false state = dead

    private int health;
    private boolean state;
    private int turns;
    private int side; // 0 - left, 1 - right
    private LatLng location;

    public Player() {
    }

    public Player(int health, boolean state, int turns, int side, LatLng location) {
        this.health = health;
        this.state = state;
        this.turns = turns;
        this.side = side;
        this.location = location;
    }

    public void action(int num) {
        if (num == 0) { //Light Attack
            this.health -= 5;
        } else if (num == 1) { //Heavy Attack
            this.health -= 10;
        } else {
            if (this.health > 35) {
                this.health = 50;
            } else { //Heal
                this.health += 15;
            }
        }
    }

    //If health is below 0, you lost.
    public boolean checkState() {
        if (this.health <= 0) {
            this.setState(false);
            return true;
        }
        return false;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getTurns() {
        return turns;
    }

    public void incrementTurn() {
        this.turns++;
    }

    public int getSide() {
        return side;
    }
    public void setSide(int side) {
        this.side = side;
    }
}
