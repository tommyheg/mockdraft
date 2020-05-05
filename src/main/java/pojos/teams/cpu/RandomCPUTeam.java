package pojos.teams.cpu;

import data.storage.DataStorer;
import pojos.Player;

public class RandomCPUTeam extends CPUTeam{

    public RandomCPUTeam(int position) {
        super(position);
    }

    @Override
    /**
     * Randomly choose one of the next X players, expanding the range as needed
     * @param dataStorer- database connector
     * @return the random player selected
     */
    public Player selectPlayer(DataStorer dataStorer) {
        int range = 10;
        Player player = selectPlayerHelper(dataStorer, range);
        int count = 1;
        while(!roomForPlayer(player.getPosition()) || unavailablePlayers.containsKey(player.getName())){
            if(count>range) range*=2;
            player = selectPlayerHelper(dataStorer, range);
            unavailablePlayers.put(player.getName(), true);
            count++;
        }
        unavailablePlayers.clear();
        return player;
    }

    /**
     * Get a random player from the database
     * @param dataStorer- database connector
     * @param range- range to select random player from
     * @return the random player selected
     */
    private Player selectPlayerHelper(DataStorer dataStorer, int range){
        return dataStorer.getRandomPlayer(range);
    }
}
