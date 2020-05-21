package pojos.teams;

import java.util.ArrayList;
import java.util.List;

public class UserTeam extends Team{

    private List<Integer> picks;

    public UserTeam(int position, int leagueSize){
        super(position);
        this.user = true;
        setPicks(leagueSize);
    }

    private void setPicks(int leagueSize){
        picks = new ArrayList<>();
        for(int i = 1; i <= 16; i++){
            int num = 0;
            if(i%2==1) num = (i-1)*leagueSize+position;
            else num = i*leagueSize-position+1;
            picks.add(num);
        }
    }

}
