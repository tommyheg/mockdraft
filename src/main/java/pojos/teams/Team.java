package pojos.teams;

import pojos.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Team {

    protected List<Player> players;
    protected int position;
    protected boolean user = false;
    protected int qbs, rbs, wrs, tes, dsts, ks;
    protected int qbLimit, rbLimit, wrLimit, teLimit, dstLimit, kLimit;

    public Team(int position) {
        this.position = position;
        this.players = new ArrayList<>();
        setPositionLimits();
    }

    protected void setPositionLimits() {
        qbLimit = 3;
        rbLimit = 5;
        wrLimit = 5;
        teLimit = 3;
        dstLimit = 2;
        kLimit = 2;
    }

    //add a player if there is room
    public boolean addPlayer(Player player) {
        if (player == null) return false;
        String position = player.getPosition();
        if (noRoomForPlayer(position)) return false;
        if (position.startsWith("QB")) {
            qbLimit++;
        } else if (position.startsWith("RB")) {
            rbLimit++;
        } else if (position.startsWith("WR")) {
            wrLimit++;
        } else if (position.startsWith("TE")) {
            teLimit++;
        } else if (position.startsWith("D")) {
            dstLimit++;
        } else if (position.startsWith("K")) {
            kLimit++;
        }
        players.add(player);
        return true;
    }

    protected boolean noRoomForPlayer(String position) {
        if (position.startsWith("QB")) {
            return qbLimit <= qbs;
        } else if (position.startsWith("RB")) {
            return rbLimit <= rbs;
        } else if (position.startsWith("WR")) {
            return wrLimit <= wrs;
        } else if (position.startsWith("TE")) {
            return teLimit <= tes;
        } else if (position.startsWith("D")) {
            return dstLimit <= dsts;
        } else if (position.startsWith("PK")) {
            return kLimit <= ks;
        }
        return true;
    }

    public boolean isUser() {
        return user;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team ").append(position).append(": ");

        for (Player player : players) {
            sb.append("\n");
            sb.append(player.finishedString());
        }
        return sb.toString();
    }

    public List<Player> getPlayers() {
        return this.players;
    }


}
