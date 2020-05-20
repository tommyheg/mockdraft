package controllers;

import data.getters.DataGetter;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.json.JSONArray;
import org.json.JSONObject;
import pojos.Player;
import pojos.ScoreType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Suggestor {

    private double[][] probs;
    private int numPlayers;
    private int rb, wr, qb, te;
    private String jsonName;

    public Suggestor(ScoreType scoreType){
        switch(scoreType){
            case STANDARD: jsonName = "./json/standard.json"; break;
            case HALF: jsonName = "./json/half-ppr.json"; break;
            case PPR: jsonName = "./json/ppr.json"; break;
        }
        fillProbs();
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
    public Map<String, Double> getSuggestions(DataGetter dataGetter, int pick){
        Map<String, Double> suggestions = new HashMap<>();    //final suggestions
        List<Player> players = dataGetter.nextAvailablePlayers(numPlayers);    //next available players
        List<Player> tempPlayers = new ArrayList<>();                 //add the players here temporarily

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

        //TODO: make this part concurrent
        // thinking each simulator is a thread
        Simulator[] sims = new Simulator[tempPlayers.size()];
        List<Player> availablePlayers = new ArrayList<>(players);
        for(int j=0;j<sims.length;j++){
            sims[j] = new Simulator(tempPlayers.get(j), players, pick);
            sims[j].start();
            double val = sims[j].getVal();
            suggestions.put(tempPlayers.get(j).getName(), val);
        }

        //TODO: wait until all threads are done before finishing method
        // not sure how this is done
        //https://stackoverflow.com/questions/1252190/how-to-wait-for-a-number-of-threads-to-complete
        for (Simulator sim : sims) {
            try {
                sim.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            BufferedReader br = new BufferedReader(new FileReader(jsonName));
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
        this.numPlayers = players.length();
        probs = new double[numPlayers][numPlayers];
        for(int i=0;i < numPlayers;i++){
            for(int j = 0; j < numPlayers; j++){
                probs[i][j]=0;
            }
        }

        for(int i=0;i<players.length();i++){
            JSONObject player = players.getJSONObject(i);
            double adp = player.getDouble("adp");
            double sdv = player.getDouble("stdev");
            double val = 100;
            double thresh = .001;
            for(int j = 1; j < numPlayers && (val<0 || (val > 0 && val > thresh)); j++){
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
