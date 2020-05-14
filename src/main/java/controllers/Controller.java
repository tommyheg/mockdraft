package controllers;

import data.DataType;
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
import webscraping.Site;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Controller {

    private final DataStorer dataStorer;
    private final int leagueSize, rounds, totalPicks;
    private int pickNumber = 1, currentPick = 1, currentRound = 1;
    private List<Team> teams;
    private Team currentTeam;
    private Logger logger = Logger.getLogger();

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
     * Add the player to the team, advance the turn, and remove the player from database
     * @param player- player to be added
     * @return whether the player was added or not
     */
    public boolean draft(Player player){
        if(!currentTeam.addPlayer(player)){
            return false;
        }
        advanceTurn();
        removePlayer(player);
        return true;
    }

    /**
     * get the player from the database
     * @param name- name of the player to get
     * @return the player (null if he isn't available)
     */
    public Player draftPlayer(String name){
        return dataStorer.getPlayer(name);
    }

    /**
     * Select a player for the cpu team
     * @return the player the cpu team drafted
     */
    public Player draftPlayerCPU(){
        CPUTeam cpuTeam = (CPUTeam) currentTeam;
        return cpuTeam.selectPlayer(dataStorer);
    }

    /**
     * Remove a player from the database
     * @param player- the player to remove
     */
    private void removePlayer(Player player){
        dataStorer.removePlayer(player);
    }

    /**
     * Advance to the next team after a player is selected
     */
    public void advanceTurn(){
        //TODO: make this a snake method
        pickNumber += 1;
        currentRound = pickNumber / leagueSize + 1;
        if(currentRound%2==1){
            currentPick += 1;
            if(currentPick > leagueSize) currentPick-=1;
        } else{
            currentPick -= 1;
            if(currentPick == 0) currentPick+=1;
        }
        currentTeam = teams.get(currentPick);
        System.out.println("Current pick: "+currentPick);
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
     * Have the data storer get the data, but only if not done that day
     */
    public void setData(){
        int limit = rounds * leagueSize;  //this will be done later
        limit = 30;
        GregorianCalendar lastDate = lastDate();
        if(updateNeeded(lastDate, limit)){
            System.out.println("Update needed.");
            logger.logWebScrape(limit);
            dataStorer.storeData(limit);
        }
    }

    /**
     * Decide whether an update of the data is required. Update if the
     * last update is over a day old or I am updating more players.
     * @param date- previous update date
     * @param limit- roughly how many players that will be gotten
     * @return whether or not an update is required
     */
    private boolean updateNeeded(GregorianCalendar date, int limit){
        GregorianCalendar currentDate = new GregorianCalendar();
        if(currentDate.get(Calendar.YEAR) > date.get(Calendar.YEAR)) return true;
        if(currentDate.get(Calendar.MONTH) > date.get(Calendar.MONTH)) return true;
        if(currentDate.get(Calendar.DAY_OF_MONTH) > date.get(Calendar.DAY_OF_MONTH)) return true;

        int lastLimit = -1;
        try{
            Scanner fileReader = new Scanner(new File("web_scraping.txt"));
            fileReader.nextLine();
            lastLimit = fileReader.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return limit > lastLimit;
    }

    /**
     * Get the last date that the data was updated
     * @return the date from web_scraping.txt
     */
    private GregorianCalendar lastDate(){
        GregorianCalendar date = null;
        try{
            Scanner fileReader = new Scanner(new File("web_scraping.txt"));
            String line = fileReader.next();
            String[] contents = line.split("/");
            date = new GregorianCalendar(Integer.parseInt(contents[0]), Integer.parseInt(contents[1])-1, Integer.parseInt(contents[2]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Check to see if the draft is complete
     * @return whether or not the draft is complete
     */
    public boolean finished(){
        return pickNumber>totalPicks;
    }

    /**
     * Clean up all loose ends (connections, etc)
     */
    public void cleanUp(){
        dataStorer.cleanUp();
    }
}
