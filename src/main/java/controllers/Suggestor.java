package controllers;

import data.getters.DataGetter;
import pojos.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Suggestor {

    public Suggestor(){

    }

    /**
     * Simulate the next 5 rounds for selecting this player.
     * This method will be difficult
     * @param player- player to be selected
     * @return the value of taking that plaeyr
     */
    private double simulate(Player player){
        //stub
        return Math.random()*10+1;
    }

    /**
     * Get suggestions for the next few players.
     * Loop through the available players,
     * add the next few players at each position,
     * start a sim for each player,
     * add that player's value to the map.
     * @return the map of player suggestions
     */
    public Map<String, Double> getSuggestions(DataGetter dataGetter){
        Map<String, Double> suggestions = new HashMap<String, Double>();    //final suggestions
        List<Player> players = dataGetter.nextAvailablePlayers(100);    //next available players
        List<Player> tempPlayers = new ArrayList<Player>();                 //add the players here temporarily

        int i = 0;
        boolean complete = false;
        //loop through players until 3 rbs gotten, 3 wrs, 2 qbs, 2 tes, etc
        while(!complete){     //repeat until all those requirements met
            //stubbish for now
            Player player = players.get(i);
            if(roomForPlayer(player, tempPlayers)) {
                tempPlayers.add(player);
                complete = gottenPlayers(tempPlayers);
            }
            i++;
        }

        for(Player player: tempPlayers){
            double val = simulate(player);
            suggestions.put(player.getName(), val);
        }

        return suggestions;
    }

    /**
     * Check to see if the temporary list of players has all
     * the requirements to sim
     * @param players- temporary list of players
     * @return whether the requirements are met
     */
    private boolean gottenPlayers(List<Player> players){
        //stub for now
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
        String position = player.getPosition();
        int count = 0;
        for(Player p: tempPlayers){
            if(p.getPosition().charAt(0) == position.charAt(0)){
                count++;
                if(count==3){
                    if(position.startsWith("RB") || position.startsWith("WR")) return false;
                } else if(count == 2){
                    if(position.startsWith("QB") || position.startsWith("TE")) return false;
                }
                //TODO: include DST and K here. Not sure what we want to do with this
            }
        }
        return true;
    }

}
