package data.getters;

import pojos.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalGetter extends DataGetter {

    private List<Player> availablePlayers;
    private Map<String, Player> playerMap;

    public LocalGetter(){
        populate();
    }

    /**
     * Populate the players list and map
     * in the constructor by getting the
     * whole list from server
     */
    private void populate(){
        availablePlayers = getAllPlayers();
        playerMap = new HashMap<>();
        for(Player p: availablePlayers){
            playerMap.put(p.getName(), p);
        }
    }


    @Override
    public List<Player> getAllPlayers(){
        return null;
    }

    /**
     * Get a list of the next N available players
     * through streams
     * @param limit- number of players to get
     * @return a list of available players
     */
    @Override
    public List<Player> nextAvailablePlayers(int limit) {
        return availablePlayers.stream()
                .filter(s->s.getTeamNum() == -1)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Remove a player from the database by
     * setting his team variable to his current team
     * @param player- player to be removed
     * @param teamNum- team he was drafted to
     */
    @Override
    public void removePlayer(Player player, int teamNum) {
        player.setTeamNum(teamNum);
    }

    /**
     * Get a player by his name
     * @param name- name of the player
     * @return the corresponding player
     */
    @Override
    public Player getPlayer(String name) {
        return playerMap.get(name);
    }

    /**
     * Get a random available player
     * @param range- range of players to get from
     * @return null if the player is already taken, otherwise the player
     */
    @Override
    public Player getRandomPlayer(int range) {
        int random = (int) (Math.random()*range) + 1;
        Player player = availablePlayers.get(random);
        if(player.getTeamNum() != -1) return null;
        return availablePlayers.get(random);
    }

    /**
     * Get the Nth player
     * @param count- the Nth position
     * @return the Nth player
     */
    @Override
    public Player getNextPlayer(int count) {
        return availablePlayers.get(count);
    }
}
