package pojos.teams.cpu;

import data.storage.DataStorer;
import pojos.Player;

public class SmartCPUTeam extends CPUTeam{


    public SmartCPUTeam(int position) {
        super(position);
    }

    @Override
    /**
     * Choose next player based on team needs or something.
     * Just make this one not predictable.
     * Maybe randomly choose a strategy in the constructor
     * to base this off of (stream qbs, bpa, etc).
     */
    public Player selectPlayer(DataStorer dataStorer) {
        //TODO: smartly select a player
        // this is more complicated
        // also must check if player is draftable- use loop
        return null;
    }
}
