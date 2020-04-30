package pojos;

import java.util.List;

public class NFLTeam {

    private final String name;
    private List<Player> players;

    public NFLTeam(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }

    public NFLTeam(String name) {
        this.name = name;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
