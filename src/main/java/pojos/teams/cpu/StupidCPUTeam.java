package pojos.teams.cpu;

import data.getters.DataGetter;
import data.storage.DataStorer;
import pojos.Player;

public class StupidCPUTeam extends CPUTeam{


    public StupidCPUTeam(int position) {
        super(position);
    }


    /**
     * Just choose the next available player
     * @param dataGetter- database connector
     * @return the player selected
     */
    @Override
    public Player selectPlayer(DataGetter dataGetter) {
        int count = 0;
        Player player = dataGetter.getNextPlayer(count);
        while(!roomForPlayer(player.getPosition()) || unavailablePlayers.containsKey(player.getName()) || player.getTeamNum() != -1){
            count++;
            player = dataGetter.getNextPlayer(count);
            if(player == null) return null;
        }
        unavailablePlayers.clear();
        return player;
    }
}
