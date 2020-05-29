package pojos.teams.cpu;

public class CPUTeamFactory {

    public CPUTeam getCPUTeam(int position, Difficulty difficulty) {
        switch (difficulty) {
            case STUPID:
                return new StupidCPUTeam(position);
            case RANDOM:
                return new RandomCPUTeam(position);
            case SMART:
                return new SmartCPUTeam(position);
        }
        return null;
    }
}
