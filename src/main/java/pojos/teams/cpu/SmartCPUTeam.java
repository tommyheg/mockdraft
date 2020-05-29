package pojos.teams.cpu;

import data.getters.DataGetter;
import pojos.Player;

public class SmartCPUTeam extends CPUTeam {


    public SmartCPUTeam(int position) {
        super(position);
    }

    //realistically select player
    @Override
    public Player selectPlayer(DataGetter dataGetter) {
        //TODO: smartly select a player
        return new Player();
    }
}
