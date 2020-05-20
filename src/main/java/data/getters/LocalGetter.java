package data.getters;

import pojos.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalGetter extends DataGetter {

    private List<Player> availablePlayers;
    private Map<String, Player> playerMap;

    public LocalGetter(){
        populate();
    }

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

    @Override
    public List<Player> nextAvailablePlayers(int limit) {
        return null;
    }

    @Override
    public void removePlayer(Player player, int teamNum) {
        player.setTeamNum(teamNum);
    }

    @Override
    public Player getPlayer(String name) {
        return playerMap.get(name);
    }

    @Override
    public Player getRandomPlayer(int range) {
        int random = (int) (Math.random()*range) + 1;
        return availablePlayers.get(random);
    }

    @Override
    public Player getNextPlayer(int count) {
        return availablePlayers.get(count);
    }
}
