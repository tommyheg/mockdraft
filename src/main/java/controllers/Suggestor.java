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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Suggestor {

    private double[][] probs;
    private int numPlayers;
    private final int userPick, leagueSize;
    private int rb, wr, qb, te;
    private String jsonName = "./json/standard.json";

    public Suggestor(ScoreType scoreType, int userPick, int leagueSize) {
        if (scoreType == ScoreType.HALF) jsonName = "./json/half-ppr.json";
        else if (scoreType == ScoreType.PPR) jsonName = "./json/ppr.json";
        this.userPick = userPick;
        this.leagueSize = leagueSize;
        fillProbs();
    }

    /**
     * Get suggestions for the next few players.
     * Loop through the available players,
     * add the next few players at each position,
     * start a sim for each player,
     * add that player's value to the map.
     *
     * @param dataGetter- used to get the list of players
     * @param round-      current round
     * @param pick-       current pick
     * @return the list of player suggestions
     */
    public List<Player> getSuggestions(DataGetter dataGetter, int round, int pick) {
        List<Player> suggestions = new ArrayList<>();   //return list to be populated
        List<Player> players = dataGetter.nextAvailablePlayers(numPlayers);    //all of the available players
        Map<String, Player> playerMap = new ConcurrentHashMap<>();  //cache of players
        for (Player p : players) {
            playerMap.put(p.getName(), p);
        }
        List<Player> simPlayers = new ArrayList<>();   //players to be simmed on

        int i = 0;
        boolean complete = false;
        //loop through players until 3 rbs gotten, 3 wrs, 2 qbs, 2 tes, etc
        while (!complete && i < players.size()) {     //repeat until all those requirements met
            Player player = players.get(i);
            if (roomForPlayer(player)) {
                //update the positions filled and add him to the sim list
                updatePositions(player.getPosition());
                simPlayers.add(player);
                complete = gottenPlayers();
            }
            i++;
        }

        Simulator[] sims = new Simulator[simPlayers.size()];
        Map<String, Double> values = new ConcurrentHashMap<>(); //cache to store player values
        for (int j = 0; j < sims.length; j++) {
            Map<String, Player> copyMap = new ConcurrentHashMap<>(playerMap);   //map of available players for simmer
            sims[j] = new Simulator(simPlayers.get(j), copyMap, values, round,
                    userPick, leagueSize, pick, probs);
//            sims[j].start();  //decide between concurrency and serial
            sims[j].simulate();
        }

        //wait until all threads are done before moving on (if we do concurrency)
        //https://stackoverflow.com/questions/1252190/how-to-wait-for-a-number-of-threads-to-complete
        for (int j = 0; j < sims.length; j++) {
//            try {
//                sims[j].join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            double val = sims[j].getVal();
            simPlayers.get(j).setValue(val);
            suggestions.add(simPlayers.get(j));
        }

        //reset player limits to 0 for next sim
        rb = 0;
        wr = 0;
        qb = 0;
        te = 0;
        return suggestions;
    }

    //check if the position requirements are met
    private boolean gottenPlayers() {
        //TODO: discuss position and position limits
        return rb == 4 && wr == 4 && qb == 2 && te == 2;
    }

    //check if there is room to add the player to be simmed
    private boolean roomForPlayer(Player player) {
        //TODO: discuss positions and position limits
        String position = player.getPosition();
        if (position.startsWith("RB")) {
            return rb < 4;
        } else if (position.startsWith("WR")) {
            return wr < 4;
        } else if (position.startsWith("QB")) {
            return qb < 2;
        } else if (position.startsWith("TE")) {
            return te < 2;
        }
        return false;
    }

    //update positions filled
    private void updatePositions(String position) {
        if (position.startsWith("RB")) {
            rb++;
        } else if (position.startsWith("WR")) {
            wr++;
        } else if (position.startsWith("QB")) {
            qb++;
        } else if (position.startsWith("TE")) {
            te++;
        }
    }

    //fill the table of probabilities from the json of players
    private void fillProbs() {
        //get the text from the json
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

        //initialize the probs table
        JSONObject root = new JSONObject(text);
        JSONArray players = root.getJSONArray("players");
        numPlayers = players.length();
        probs = new double[numPlayers][numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            for (int j = 0; j < numPlayers; j++) {
                probs[i][j] = 0;
            }
        }

        //update each player's prob at each position
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            double adp = player.getDouble("adp");
            double sdv = player.getDouble("stdev");
            double val = 100;
            double thresh = .001;
            //do this until the prob is negligible
            for (int j = 1; j < numPlayers && (val < 0 || (val > 0 && val > thresh)); j++) {
                val = getPickProb(adp, sdv, j);
                probs[i][j] = val;
            }
        }
    }

    //calculate the prob of the player being there at that pick
    private double getPickProb(double adp, double sdv, int pick) {
        if (pick == 1) {
            return pick - adp;
        }
        NormalDistribution nd = new NormalDistribution(adp, sdv);
        return (1 - nd.cumulativeProbability(pick - 1)) * (pick - adp);
    }

}
