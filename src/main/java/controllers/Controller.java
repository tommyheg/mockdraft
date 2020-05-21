package controllers;

import data.DataType;
import data.getters.DataGetter;
import data.getters.LocalGetter;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
import logger.Logger;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.Team;
import pojos.teams.UserTeam;
import pojos.teams.cpu.CPUTeam;
import pojos.teams.cpu.CPUTeamFactory;
import pojos.teams.cpu.Difficulty;

import java.util.*;

public class Controller {

    private DataGetter dataGetter;
    private Suggestor suggestor;
    private DataStorer dataStorer;
    private final int leagueSize, rounds, totalPicks, userPick;
    private int pickNumber = 1, currentPick = 1, currentRound = 1, roundPick = 1;
    private List<Team> teams;
    private Team currentTeam;

    public Controller(ScoreType scoreType, int leagueSize, int userPick, Difficulty difficulty){
        this.leagueSize = leagueSize;
        this.rounds = 16;
        this.userPick = userPick;
        this.totalPicks = rounds * leagueSize;
        initializeTeams(difficulty, userPick);
        this.currentTeam = teams.get(0);

        this.suggestor = new Suggestor(scoreType, userPick, leagueSize);
        this.dataStorer = new DataStorerFactory().getDataStorer(scoreType, DataType.SQL, leagueSize);
        this.dataGetter = new LocalGetter(scoreType);
    }

    public Map<String, Double> getSuggestions(){
        return suggestor.getSuggestions(dataGetter, currentRound, pickNumber);
    }

    /**
     * Get the player-to-value suggestions from the suggestor
     * @return a sorted map of players to values
     */
    public List<String> sortSuggestions(Map<String, Double> map){
        //copy over everything into another map
        Map<String, Double> suggestions = new HashMap<>();
        for(String s: map.keySet()){
            suggestions.put(s, map.get(s));
        }
        List<String> sorted = new ArrayList<>();

        while(suggestions.size() > 0){
            String player = "";
            double max = -1;
            for(String s: suggestions.keySet()){
                double val = suggestions.get(s);
                if(val > max){
                    player = s;
                    max = val;
                }
            }
            sorted.add(player);
            suggestions.remove(player);
        }
        return sorted;
    }

    /**
     * Add the player to the team, advance the turn, and remove the player from database
     * @param player- player to be added
     * @return whether the player was added or not
     */
    public boolean draft(Player player){
        if(!currentTeam.addPlayer(player)){
            return false;
        }
        player.setPick(currentRound, roundPick);
        removePlayer(player, currentPick - 1);
        advanceTurn();
        return true;
    }

    /**
     * get the player from the database
     * @param name- name of the player to get
     * @return the player (null if he isn't available)
     */
    public Player draftPlayer(String name){
        if(name.split(" ").length < 2) return null;
        return dataGetter.getPlayer(name);
    }

    /**
     * Select a player for the cpu team
     * @return the player the cpu team drafted
     */
    public Player draftPlayerCPU(){
        CPUTeam cpuTeam = (CPUTeam) currentTeam;
        return cpuTeam.selectPlayer(dataGetter);
    }

    /**
     * Remove a player from the database
     * @param player- the player to remove
     */
    private void removePlayer(Player player, int teamNum){
        dataGetter.removePlayer(player, teamNum);
    }

    /**
     * Advance to the next team after a player is selected.
     * Maybe figure out a more elegant way to do this. rn this looks like shit
     */
    public void advanceTurn(){
        pickNumber += 1;

        if(currentRound % 2 == 1){
            currentPick += 1;
            if(currentPick > leagueSize) currentPick -= 1;
        } else{
            currentPick -= 1;
            if(currentPick == 0) currentPick += 1;
        }

        currentRound = (pickNumber-1) / leagueSize + 1;
        if(currentRound % 2 == 0) roundPick = (leagueSize + 1) - currentPick;
        else roundPick = currentPick;
        currentTeam = teams.get(currentPick-1);
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
        return dataGetter.nextAvailablePlayers(limit);
    }

    /**
     * Initialize the teams to all be CPU except the user pick
     * @param difficulty- the difficulty each CPU team will be drafting with
     * @param userPick- the pick that the user holds
     */
    private void initializeTeams(Difficulty difficulty, int userPick){
        this.teams = new ArrayList<>(leagueSize);
        for(int i=0;i<leagueSize;i++){
            teams.add(new CPUTeamFactory().getCPUTeam(i+1, difficulty));
        }
        teams.set(userPick-1, new UserTeam(userPick, leagueSize));
    }

    /**
     * Check to see if the draft is complete
     * @return whether or not the draft is complete
     */
    public boolean finished(){
        return pickNumber>totalPicks;
//        return pickNumber > 50;
    }

    public List<Team> getTeams() { return teams; }

    public int getRound(){
        return currentRound;
    }

    public int getCurrentPick(){
        return roundPick;
    }

    /**
     * Finish the draft by resetting the data and closing connections, etc.
     */
    public void cleanUp(){
        dataStorer.cleanUp();
        dataGetter.cleanUp();
    }

}
