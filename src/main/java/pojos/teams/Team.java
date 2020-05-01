package pojos.teams;

import pojos.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Team {

    protected List<Player> players;
    protected int position;
    //maybe use a data structure for these...?
    protected int qbs, rbs, wrs, tes, dsts, ks;
    protected int qbLimit, rbLimit, wrLimit, teLimit, dstLimit, kLimit;

    public Team(int position){
        this.position = position;
        this.players = new ArrayList<Player>();
        this.setLimits();
    }

    /**
     * Set the limits of each position. Subject to change
     */
    protected void setLimits(){
        qbLimit = 3;
        rbLimit = 5;
        wrLimit = 5;
        teLimit = 3;
        dstLimit = 2;
        kLimit = 2;
    }

    public abstract void selectPlayer();

    protected boolean roomForPlayer(String position){
        if(position.startsWith("QB")){
            return qbLimit>qbs;
        } else if(position.startsWith("RB")){
            return rbLimit>rbs;
        } else if(position.startsWith("WR")){
            return wrLimit>wrs;
        } else if(position.startsWith("TE")){
            return teLimit>tes;
        } else if(position.startsWith("D")){
            return dstLimit>dsts;
        } else if(position.startsWith("K")){
            return kLimit>ks;
        }
        return false;
    }
}
