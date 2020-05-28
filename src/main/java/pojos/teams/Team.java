package pojos.teams;

import pojos.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Team {

    protected List<Player> players;
    protected int position;
    protected boolean user = false;
    //maybe use a data structure for these...?
    protected int qbs, rbs, wrs, tes, dsts, ks;
    protected int qbLimit, rbLimit, wrLimit, teLimit, dstLimit, kLimit;

    public Team(int position){
        this.position = position;
        this.players = new ArrayList<>();
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

    /**
     * Add a player to the team if there is room
     * @param player- player to be added
     * @return whether or not the player was added
     */
    public boolean addPlayer(Player player){
        String position = player.getPosition();
        if(!roomForPlayer(position)) return false;  //this may be used somewhere else and be unnecessary
        if(position.startsWith("QB")){
            qbLimit++;
        } else if(position.startsWith("RB")){
            rbLimit++;
        } else if(position.startsWith("WR")){
            wrLimit++;
        } else if(position.startsWith("TE")){
            teLimit++;
        } else if(position.startsWith("D")){
            dstLimit++;
        } else if(position.startsWith("K")){
            kLimit++;
        }
        players.add(player);
        return true;
    }

    /**
     * Check if there is room for the player on the team
     * @param position- position of the player
     * @return whether or not there is room for the player
     */
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
        } else if(position.startsWith("PK")){
            return kLimit>ks;
        }
        return false;
    }

    public boolean isUser(){
        return user;
    }

    /**
     * Maybe format teams later
     * @return a string of the team
     */
    public String toString(){
//        StringBuilder sb = new StringBuilder("");
//        sb.append("\nQB: ");
//        sb.append(getPlayer("QB", 1));
//        sb.append("\nRB: ");
//        sb.append(getPlayer("RB", 1));
//        sb.append("\nRB: ");
//        sb.append(getPlayer("RB", 2));
//        sb.append("\nWR: ");
//        sb.append(getPlayer("WR", 1));
//        sb.append("\nWR: ");
//        sb.append(getPlayer("WR", 2));
//        sb.append("\nTE: ");
//        sb.append(getPlayer("TE", 1));
//        sb.append("\nFLEX: ");
//        sb.append(getPlayer("FLEX", 1));
//        sb.append("\nDST: ");
//        sb.append(getPlayer("DST", 1));
//        sb.append("\nK: ");
//        sb.append(getPlayer("K", 1));
//        for(int i=0;i<players.size()-9;i++) {
//            sb.append("\nBench: ");
//            sb.append(getBench(i+1));
//        }
//        return sb.toString();
        StringBuilder sb = new StringBuilder("");

        sb.append("Team "+position+": ");
        for(Player player: players){
            sb.append("\n");
            sb.append(player.finishedString());
        }

        return sb.toString();
    }

    //stub for formatting teams later
    private String getPlayer(String position, int num){
        return "";
    }

    //stub for formatting teams later
    private String getBench(int num){
        return "";
    }

    public List<Player> getPlayers() { return this.players; }


}
