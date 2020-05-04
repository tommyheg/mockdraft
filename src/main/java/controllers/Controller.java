package controllers;

import data.DataType;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.Team;
import pojos.teams.UserTeam;
import pojos.teams.cpu.CPUTeamFactory;
import pojos.teams.cpu.Difficulty;
import webscraping.Site;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private DataStorer dataStorer;
    private int leagueSize, rounds, totalPicks, pickNumber = 1, currentPick = 1, currentRound = 1;
    private List<Team> teams;
    private Team currentTeam;

    public Controller(Site site, ScoreType scoreType, DataType dataType,
                      int leagueSize, int userPick, Difficulty difficulty){
        this.dataStorer = new DataStorerFactory().getDataStorer(site, scoreType, dataType);
        this.leagueSize = leagueSize;
        this.rounds = 16;
        this.totalPicks = rounds * leagueSize;
        initializeTeams(difficulty, userPick);
        this.currentTeam = teams.get(0);
    }

    /**
     * Draft a player
     * @return the player drafted
     */
    public Player draftPlayer(){
        Player player = currentTeam.selectPlayer();
        return player;
    }

    /**
     * Remove a player from the database
     * @param player- the player to remove
     */
    public void removePlayer(Player player){
        dataStorer.removePlayer(player);
    }

    /**
     * Advance to the next team after a player is selected
     */
    public void advanceTurn(){
        //TODO: make this a snake method
        pickNumber += 1;
        currentRound = pickNumber % leagueSize;
        currentPick = (currentPick+1) % leagueSize;
        currentTeam = teams.get(currentPick);
    }

    /**
     * Check if the current team is a user or not
     * @return whether or not the current team is a user
     */
    public boolean userTurn(){
        return currentTeam.isUser();
    }

    /**
     * Get the next X available players from the data storer
     * @param limit- number of players to get
     * @return list of available players
     */
    public List<Player> nextAvailablePlayers(int limit){
        return dataStorer.nextAvailablePlayers(limit);
    }

    /**
     * Initialize the teams to all be CPU except the user pick
     * @param difficulty- the difficulty each CPU team will be drafting with
     * @param userPick- the pick that the user holds
     */
    private void initializeTeams(Difficulty difficulty, int userPick){
        this.teams = new ArrayList<Team>(leagueSize);
        for(int i=0;i<leagueSize;i++){
            teams.add(new CPUTeamFactory().getCPUTeam(i+1, difficulty));
        }
        teams.set(userPick, new UserTeam(userPick));
    }

    /**
     * Check to see if the draft is complete
     * @return whether or not the draft is complete
     */
    public boolean finished(){
        return pickNumber>totalPicks;
    }

    /**
     * Have the data storer get the data, but only if not done that day
     */
    public void setData(){
        //TODO: have a way to check if I have gotten the data for that day
        //use a .txt file who's only purpose is to store the time of data retrieval


//        int limit = rounds * leagueSize;  //this will be done later
        int limit = 20;
        dataStorer.storeData(limit);
    }
}
