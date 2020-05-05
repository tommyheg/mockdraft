package pojos.teams.cpu;

import pojos.Player;
import pojos.teams.Team;

public abstract class CPUTeam extends Team {

    public CPUTeam(int position){
        super(position);
    }

    public abstract Player selectPlayer();
}
