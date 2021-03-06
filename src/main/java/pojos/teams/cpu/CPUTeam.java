package pojos.teams.cpu;

import data.getters.DataGetter;
import pojos.Player;
import pojos.teams.Team;

import java.util.HashMap;
import java.util.Map;

public abstract class CPUTeam extends Team {

    protected Map<String, Boolean> unavailablePlayers;

    public CPUTeam(int position) {
        super(position);
        unavailablePlayers = new HashMap<>();
    }

    public abstract Player selectPlayer(DataGetter dataGetter);
}
