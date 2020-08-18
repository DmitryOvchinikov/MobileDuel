package com.example.mobileduel;

public class Player {

    //true state = alive, false state = dead

    private int health;
    private boolean state;
    private int turns;

    public Player() {
    }

    public Player(int health, boolean state, int turns) {
        this.health = health;
        this.state = state;
        this.turns = turns;
    }

    public void action(int num) {
        if (num == 0) { //Light Attack
            this.health -= 5;
        } else if (num == 1) { //Heavy Attack
            this.health -= 10;
        } else {
            if (this.health > 35) {
                this.health = 50;
            } else {
                this.health += 15;
            }
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

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }
}
