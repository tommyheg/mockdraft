package controllers;

import pojos.Player;

import java.util.List;

public class Simulator extends Thread {

    private Player player;
    private double val;
    private List<Player> availablePlayers;
    private int pick;

    public Simulator(Player player, List<Player> availablePlayers, int pick){
        this.player = player;
        this.availablePlayers = availablePlayers;
        this.pick = pick;
    }

    @Override
    public void run() {
        simulate();
    }

    /**
     * Simulate 5 rounds deep and return the value you get with that player
     */
    private void simulate(){
        //TODO: simulate and update val

    }

    public double getVal(){
        return val;
    }
}
