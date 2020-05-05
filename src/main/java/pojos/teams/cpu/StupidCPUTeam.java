package pojos.teams.cpu;

import data.storage.DataStorer;
import pojos.Player;

public class StupidCPUTeam extends CPUTeam{


    public StupidCPUTeam(int position) {
        super(position);
    }

    @Override
    /**
     * Just choose the next available player
     * @param dataStorer- database connector
     * @return the player selected
     */
    public Player selectPlayer(DataStorer dataStorer) {
        int count = 0;
        Player player = dataStorer.getNextPlayer(count);
        while(!roomForPlayer(player.getPosition()) || unavailablePlayers.containsKey(player.getName())){
            count++;
            player = dataStorer.getNextPlayer(count);
        }
        unavailablePlayers.clear();
        return player;
    }
}
