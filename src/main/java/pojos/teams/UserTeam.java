package pojos.teams;

import pojos.Player;

public class UserTeam extends Team{


    public UserTeam(int position){
        super(position);
        this.user = true;
    }

    @Override
    /**
     * Select player for the user. Must notify someone who is chosen
     * so they are taken off of data.
     */
    public Player selectPlayer() {
        return null;
    }
}
