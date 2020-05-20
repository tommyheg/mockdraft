package pojos.teams.cpu;

import data.getters.DataGetter;
import data.storage.DataStorer;
import pojos.Player;

public class RandomCPUTeam extends CPUTeam{

    public RandomCPUTeam(int position) {
        super(position);
    }


    /**
     * Randomly choose one of the next X players, expanding the range as needed
     * @param dataGetter- database connector
     * @return the random player selected
     */
    @Override
    public Player selectPlayer(DataGetter dataGetter) {
        int range = 10;
        Player player = selectPlayerHelper(dataGetter, range);
        int count = 1;
        while(!roomForPlayer(player.getPosition()) || unavailablePlayers.containsKey(player.getName()) || player.getTeamNum() != -1){
            if(count>range) range*=2;
            player = selectPlayerHelper(dataGetter, range);
            unavailablePlayers.put(player.getName(), true);
            count++;
        }
        unavailablePlayers.clear();
        return player;
    }

    /**
     * Get a random player from the database
     * @param dataGetter- database connector
     * @param range- range to select random player from
     * @return the random player selected
     */
    private Player selectPlayerHelper(DataGetter dataGetter, int range){
        return dataGetter.getRandomPlayer(range);
    }
}
