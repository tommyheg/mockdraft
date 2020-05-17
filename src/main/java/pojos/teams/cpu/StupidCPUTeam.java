package pojos.teams.cpu;

import data.getters.DataGetter;
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
    public Player selectPlayer(DataGetter dataGetter) {
        int count = 0;
        Player player = dataGetter.getNextPlayer(count);
        while(!roomForPlayer(player.getPosition()) || unavailablePlayers.containsKey(player.getName())){
            count++;
            player = dataGetter.getNextPlayer(count);
        }
        unavailablePlayers.clear();
        return player;
    }
}
