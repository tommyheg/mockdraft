package pojos.teams.cpu;

public class StupidCPUTeam extends CPUTeam{


    public StupidCPUTeam(int position) {
        super(position);
    }

    @Override
    /**
     * Just choose the next available player
     */
    public void selectPlayer() {

    }
}
