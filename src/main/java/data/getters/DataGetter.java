package data.getters;

import pojos.Player;

import java.util.List;

public abstract class DataGetter {

    public abstract List<Player> nextAvailablePlayers(int limit);

    public abstract void removePlayer(Player player, int teamNum);

    public abstract Player getPlayer(String name);

    public abstract Player getRandomPlayer(int range);

    public abstract Player getNextPlayer(int count);

    public abstract List<Player> getAllPlayers();

}
