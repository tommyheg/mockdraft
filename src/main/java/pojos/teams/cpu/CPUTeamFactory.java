package pojos.teams.cpu;

public class CPUTeamFactory {

    public CPUTeam getCPUTeam(int position, Difficulty difficulty){
        if(difficulty==Difficulty.STUPID){
            return new StupidCPUTeam(position);
        } else if(difficulty==Difficulty.RANDOM){
            return new RandomCPUTeam(position);
        } else if(difficulty==Difficulty.SMART){
            return new SmartCPUTeam(position);
        }
        return null;
    }
}
