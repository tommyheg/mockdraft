package pojos.teams.cpu;

import pojos.Player;

public class StupidCPUTeam extends CPUTeam{


    public StupidCPUTeam(int position) {
        super(position);
    }

    @Override
    /**
     * Just choose the next available player
     */
    public Player selectPlayer() {
        return null;
    }
}
