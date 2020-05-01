package pojos.teams;

public class UserTeam extends Team{


    public UserTeam(int position){
        super(position);
    }

    @Override
    /**
     * Select player for the user. Must notify someone who is chosen
     * so they are taken off of data.
     */
    public void selectPlayer() {

    }
}
