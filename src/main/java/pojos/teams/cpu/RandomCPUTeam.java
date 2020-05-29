package pojos.teams.cpu;

import data.getters.DataGetter;
import pojos.Player;

public class RandomCPUTeam extends CPUTeam {

    public RandomCPUTeam(int position) {
        super(position);
    }

    //randomly selects player, repeating until an available player is chosen
    @Override
    public Player selectPlayer(DataGetter dataGetter) {
        int range = 10;
        Player player = dataGetter.getRandomPlayer(range);
        int count = 1;
        while (noRoomForPlayer(player.getPosition()) || unavailablePlayers.containsKey(player.getName()) || player.getTeamNum() != -1) {
            if (count > range) range *= 2;  //range updates if the entire range is unavailable
            player = dataGetter.getRandomPlayer(range);
            unavailablePlayers.put(player.getName(), true);
            count++;
        }
        unavailablePlayers.clear();
        return player;
    }
}
