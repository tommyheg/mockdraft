package data.getters;

import pojos.Player;

import java.util.List;
import java.util.Map;

public class LocalGetter extends DataGetter {

    private List<Player> availablePlayers;
    private Map<String, Player> playerMap;

    public LocalGetter(){
        populate();
    }

    private void populate(){
        this.availablePlayers = getAllPlayers();
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
    public void removePlayer(Player player) {

    }

    @Override
    public Player getPlayer(String name) {
        return null;
    }

    @Override
    public Player getRandomPlayer(int range) {
        int random = (int) (Math.random()*range) + 1;
        return getNextPlayer(random);
    }

    @Override
    public Player getNextPlayer(int count) {
        return null;
    }
}
