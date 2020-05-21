package controllers;

import pojos.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Simulator extends Thread {

    private Player player;
    private double val;
    private Map<String, Player> playerMap;
    private int userPick, round, leagueSize, pick;
    private Map<String, Double> values;
    private double[][] probs;
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

    @Override
    public void run() {
        simulate();
    }

    /**
     * Simulate 5 rounds deep and return the value you get with that player
     */
    private void simulate(){
        //TODO: run a bfs to get all paths
        // update the total val along the way
        // store in hashmap
        // --for dynamic programming purposes
        val = simHelper(player, round, pick);
//        System.out.println("Val in simulate: "+val);
    }

    /**
     * Run a bfs starting at that player and pick.
     * @param player- player to start bfs at
     * @param round- starting round number
     * @param pick- starting pick number
     * @return
     */
    private double simHelper(Player player, int round, int pick){
        if(player == null) return 0;
        playerMap.remove(player.getName());
        if(values.containsKey(player.getName())) return values.get(player.getName());
//        if(round > this.round + 5) return 0;

        //this is where we need to figure out the value variable
        //how is it weighted??
        //divided by adp of player because smaller adp is better??
        //or something else??
        double prob = getProb(player, pick);
        if(prob == 100) return 0;
        double weight = 1/player.getADP();
        double curVal = prob * weight;
        //https://www.javacodegeeks.com/2011/05/avoid-concurrentmodificationexception.html
        Iterator<String> it = playerMap.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            curVal+=simHelper(playerMap.get(key), round + 1, getPick(round));
        }
        //this will require locks because of concurrency
        //https://stackoverflow.com/questions/5861894/how-to-synchronize-or-lock-upon-variables-in-java
        synchronized (lock) {
            values.put(player.getName(), curVal);
        }
        return curVal;
    }

    /**
     * Get the probability of that player being there at that pick
     * @param player- player to get prob of
     * @param pick- pick to get prob of
     * @return the value in the probs table
     */
    private double getProb(Player player, int pick){
        if(player.getRank() >= probs.length || pick >= probs[0].length) return 100;
        return probs[player.getRank()][pick];
    }

    /**
     * Get the next pick based on round, userPick, and leagueSizes
     * @param round- current round
     * @return the next pick that the user has
     */
    private int getPick(int round){
        if(round % 2 == 1) return round * leagueSize + userPick;
        return (round+1) * leagueSize - userPick + 1;
    }

    public double getVal(){
        return val;
    }
}
