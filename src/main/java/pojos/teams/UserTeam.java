package pojos.teams;

import java.util.ArrayList;
import java.util.List;

public class UserTeam extends Team {

    private List<Integer> picks;

    public UserTeam(int position, int leagueSize) {
        super(position);
        user = true;
        setPicks(leagueSize);
    }

    //set the user's pick numbers (used to tell them how many more picks until their next)
    private void setPicks(int leagueSize) {
        picks = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            int num;
            if (i % 2 == 1) num = (i - 1) * leagueSize + position;
            else num = i * leagueSize - position + 1;
            picks.add(num);
        }
    }

    public List<Integer> getPicks() {
        return picks;
    }

}
