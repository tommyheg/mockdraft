package controllers;

import data.getters.DataGetter;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.json.JSONArray;
import org.json.JSONObject;
import pojos.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Suggestor {

    double[][] probs;
    int numPlayers = 213, picks = 213;
    int rb, wr, qb, te;

    public Suggestor(){
        probs = new double[numPlayers][picks];
        for(int i=0;i<numPlayers;i++){
            for(int j = 0; j< picks; j++){
                probs[i][j]=0;
            }
        }
        fillProbs();
    }

    /**
     * Simulate the next 5 rounds for selecting this player.
     * This method will be difficult
     * @param player- player to be selected
     * @return the value of taking that player
     */
    private double simulate(Player player){
        //TODO: simulate the next 5 rounds of a player
        // We might need a list of available players
        // maybe have another dataset so we don't alter the first one
        // maybe run these in parallel with each other for each player
        return Math.random()*10+1;
    }

    /**
     * Get suggestions for the next few players.
     * Loop through the available players,
     * add the next few players at each position,
     * start a sim for each player,
     * add that player's value to the map.
     * @param dataGetter- used to get the list of players
     * @return the map of player suggestions
     */
    public Map<String, Double> getSuggestions(DataGetter dataGetter){
        Map<String, Double> suggestions = new HashMap<String, Double>();    //final suggestions
        List<Player> players = dataGetter.nextAvailablePlayers(numPlayers);    //next available players
        List<Player> tempPlayers = new ArrayList<Player>();                 //add the players here temporarily

        int i = 0;
        boolean complete = false;
        //loop through players until 3 rbs gotten, 3 wrs, 2 qbs, 2 tes, etc
        while(!complete){     //repeat until all those requirements met
            Player player = players.get(i);
            if(roomForPlayer(player, tempPlayers)) {
                updatePositions(player.getPosition());
                tempPlayers.add(player);
                complete = gottenPlayers(tempPlayers);
            }
            i++;
        }

        for(Player player: tempPlayers){
            double val = simulate(player);
            suggestions.put(player.getName(), val);
        }

        rb = 0; wr = 0; qb = 0; te = 0;
        return suggestions;
    }

    /**
     * Check to see if the temporary list of players has all
     * the requirements to sim
     * @param players- temporary list of players
     * @return whether the requirements are met
     */
    private boolean gottenPlayers(List<Player> players){
        //TODO: discuss position and position limits
//        return rb == 3 && wr == 3 && qb == 2 && te == 2;
        return players.size()>8;
    }

    /**
     * Check to see if there is room for the player in the
     * tempPlayers list. Check to see if the list has
     * reached the max for the player's position.
     * @param player- player to be added
     * @return if there is room for the player
     */
    private boolean roomForPlayer(Player player, List<Player> tempPlayers){
        //TODO: discuss positions and position limits
        String position = player.getPosition();
        if(position.startsWith("RB")){
            return rb < 3;
        } else if(position.startsWith("WR")){
            return wr < 3;
        } else if(position.startsWith("QB")){
            return qb < 2;
        } else if(position.startsWith("TE")){
            return te < 2;
        }
        return true;
    }

    private void updatePositions(String position){
        if(position.startsWith("RB")){
            rb++;
        } else if(position.startsWith("WR")){
            wr++;
        } else if(position.startsWith("QB")){
            qb++;
        } else if(position.startsWith("TE")){
            te++;
        }
    }

    /**
     * Fill the probs 2-D array by parsing json file and doing math
     */
    private void fillProbs(){
        String text = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("players.json"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line).append('\n');
                line = br.readLine();
            }
            text = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject root = new JSONObject(text);
        JSONArray players = root.getJSONArray("players");
        for(int i=0;i<players.length();i++){
            JSONObject player = players.getJSONObject(i);
            double adp = player.getDouble("adp");
            double sdv = player.getDouble("stdev");
            double val = 100;
            double thresh = .001;
            for(int j = 1; j<picks && (val<0 || (val > 0 && val > thresh)); j++){
                val = algo(adp, sdv, j);
                probs[i][j] = val;
            }
        }
    }

    /**
     * Calculate the prob that the player will be available at that pick
     * @param adp- adp of the player
     * @param sdv- sdv of the player
     * @param pick- current pick number to calculate prob for
     * @return the prob for that position
     */
    private double algo(double adp, double sdv, int pick){
        if(pick==1){
            return pick-adp;
        }
        else {
            NormalDistribution nd = new NormalDistribution(adp, sdv);
            return (1-nd.cumulativeProbability(pick-1))*(pick-adp);
        }
    }

}
