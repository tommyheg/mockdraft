package pojos.teams;

public class UserTeam extends Team{


    public UserTeam(int position){
        super(position);
        this.user = true;
    }

}
