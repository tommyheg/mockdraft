package pojos.teams.cpu;

import data.storage.DataStorer;
import pojos.Player;
import pojos.teams.Team;

import java.util.HashMap;
import java.util.Map;

public abstract class CPUTeam extends Team {

    protected Map<String, Boolean> unavailablePlayers;

    public CPUTeam(int position){
        super(position);
        unavailablePlayers = new HashMap<String, Boolean>();
    }

    public abstract Player selectPlayer(DataStorer dataStorer);
}
