package controllers;

import pojos.Player;

import java.util.*;

public class Simulator extends Thread {

    private final Player player;
    private double val;
    private final Map<String, Player> playerMap;
    private final int userPick, round, leagueSize, pick;
    private final Map<String, Double> values;
    private final double[][] probs;
    private final Object lock = new Object();

    public Simulator(Player player, Map<String, Player> playerMap, Map<String, Double> values,
                     int userPick, int round, int leagueSize, int pick, double[][] probs){
        this.player = player;
        this.userPick = userPick;
        this.values = values;
        this.round = round;
        this.leagueSize = leagueSize;
        this.pick = pick;
        this.probs = probs;
        this.playerMap = playerMap;
        this.playerMap.remove(player.getName());
    }

    //we may want to make this class run in parallel with other simulators
    @Override
    public void run() {
        simulate();
    }

    public void simulate(){
        val = simHelper(player, round, pick);
    }

    //run a bfs starting at that player, pick, and round
    private double simHelper(Player player, int round, int pick){
        //run base cases by updating cache and checking for rounds
        if(player == null) return 0;
        playerMap.remove(player.getName() + round);
        if(values.containsKey(player.getName() + round)) return values.get(player.getName() + round);
        if(round > this.round + 16) return 0;   //maybe don't need this

        //TODO: fix the algorithm, it is pretty fucked up
        Double prob = getProb(player, pick);
        if(prob == null) return 0;   //if outside the array, the prob doesn't exist
        double weight = 1/(player.getADP());
        double curVal = prob * weight;
        //https://www.javacodegeeks.com/2011/05/avoid-concurrentmodificationexception.html
        for (String key : playerMap.keySet()) {
            curVal += simHelper(playerMap.get(key), round + 1, nextUserPick(round));
        }
        //this will require locks because of concurrency
        //https://stackoverflow.com/questions/5861894/how-to-synchronize-or-lock-upon-variables-in-java
        synchronized (lock) {
            values.put(player.getName() + round, curVal);
        }
        return curVal;
    }

    private Double getProb(Player player, int pick){
        if(player.getRank() >= probs.length || pick >= probs[0].length) return null;
        return probs[player.getRank()][pick];
    }

    private int nextUserPick(int round){
        return round % 2 == 1 ? round * leagueSize + userPick : (round + 1) * leagueSize - userPick + 1;
    }

    public double getVal(){
        return val;
    }
}
