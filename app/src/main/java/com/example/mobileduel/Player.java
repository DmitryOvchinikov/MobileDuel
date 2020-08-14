package com.example.mobileduel;

public class Player {

    //true state = alive, false state = dead

    private int health;
    private boolean state;

    public Player() {
    }

    public Player(int health, boolean state) {
        this.health = health;
        this.state = state;
    }

    public void action(int num) {
        if (num == 0) { //Light Attack
            this.health -= 5;
        } else if (num == 1) { //Heavy Attack
            this.health -= 10;
        } else {
            this.health += 15; //Heal
        }
    }

    //If health is below 0, you lost.
    public boolean checkState() {
        if (this.health <= 0) {
            this.setState(false);
            return false;
        }
        return true;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
