package pojos.teams;

import pojos.Player;

import java.util.ArrayList;

public abstract class Team {

    protected ArrayList<Player> players;
    //maybe use a data structure for these...?
    protected int qbs, rbs, wrs, tes, dsts, ks;
    protected int qbLimit, rbLimit, wrLimit, teLimit, dstLimit, kLimit;

    public abstract void selectPlayer();
}
