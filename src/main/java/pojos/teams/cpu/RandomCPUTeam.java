package pojos.teams.cpu;

import pojos.Player;

public class RandomCPUTeam extends CPUTeam{


    public RandomCPUTeam(int position) {
        super(position);
    }

    @Override
    /**
     * Randomly choose one of the next X players
     */
    public Player selectPlayer() {
        return null;
    }
}
