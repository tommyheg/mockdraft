package pojos.teams.cpu;

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
    public void selectPlayer() {

    }
}
