package controllers;

import data.DataType;
import data.getters.DataGetter;
import data.getters.SQLGetter;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
import data.storage.SQLStorer;
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

    private DataGetter dataGetter;
    private Suggestor suggestor;
    private final int leagueSize, rounds, totalPicks, userPick;
    private int pickNumber = 1, currentPick = 1, currentRound = 1, roundPick = 1;
    private List<Team> teams;
    private Team currentTeam;
    private final Logger logger = Logger.getLogger();

    public Controller(ScoreType scoreType, int leagueSize, int userPick, Difficulty difficulty){
        this.leagueSize = leagueSize;
        this.rounds = 16;
        this.userPick = userPick;
        this.totalPicks = rounds * leagueSize;
        initializeTeams(difficulty, userPick);
        this.currentTeam = teams.get(0);
        this.dataGetter = new SQLGetter(scoreType);
        this.suggestor = new Suggestor();
        setData();
    }

    /**
     * Copy over the data at the very beginning
     */
    private void setData(){
        DataStorer dataStorer = new DataStorerFactory().getDataStorer(Site.FFCALCULATOR,
                ScoreType.STANDARD, DataType.SQL, leagueSize);
        dataStorer.copyData();
    }

    public Map<String, Double> getSuggestions(){
        return suggestor.getSuggestions(dataGetter, userPick);
    }

    /**
     * Get the player-to-value suggestions from the suggestor
     * @return a sorted map of players to values
     */
    public List<String> sortSuggestions(Map<String, Double> map){
        //copy over everything into another map
        Map<String, Double> suggestions = new HashMap<String, Double>();
        for(String s: map.keySet()){
            suggestions.put(s, map.get(s));
        }
        List<String> sorted = new ArrayList<String>();

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
    private void removePlayer(Player player){
        dataGetter.removePlayer(player);
    }

    /**
     * Advance to the next team after a player is selected
     */
    public void advanceTurn(){
//        System.out.println("\nRound "+currentRound+", Pick "+roundPick+", Team "+currentPick);

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
        teams.set(userPick-1, new UserTeam(userPick));
    }

//    /**
//     * Have the data storer get the data, but only if not done that day
//     */
//    public void setData(){
//        int limit = rounds * leagueSize;  //this will be done later
//        limit = 250;
//        GregorianCalendar lastDate = lastDate();
////        if(updateNeeded(lastDate, limit)){
////            logger.logWebScrape(limit);
////            dataStorer.storeData(limit);
////        }
//        dataStorer.copyData();
//    }

//    /**
//     * Decide whether an update of the data is required. Update if the
//     * last update is over a day old or I am updating more players.
//     * @param date- previous update date
//     * @param limit- roughly how many players that will be gotten
//     * @return whether or not an update is required
//     */
//    private boolean updateNeeded(GregorianCalendar date, int limit){
//        GregorianCalendar currentDate = new GregorianCalendar();
//        if(currentDate.get(Calendar.YEAR) > date.get(Calendar.YEAR)) return true;
//        if(currentDate.get(Calendar.MONTH) > date.get(Calendar.MONTH)) return true;
//        if(currentDate.get(Calendar.DAY_OF_MONTH) > date.get(Calendar.DAY_OF_MONTH)) return true;
//
//        int lastLimit = -1;
//        try{
//            Scanner fileReader = new Scanner(new File("web_scraping.txt"));
//            fileReader.nextLine();
//            lastLimit = fileReader.nextInt();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return limit > lastLimit;
////        return false;
//    }

//    /**
//     * Get the last date that the data was updated
//     * @return the date from web_scraping.txt
//     */
//    private GregorianCalendar lastDate(){
//        GregorianCalendar date = null;
//        try{
//            Scanner fileReader = new Scanner(new File("web_scraping.txt"));
//            String line = fileReader.next();
//            String[] contents = line.split("/");
//            date = new GregorianCalendar(Integer.parseInt(contents[0]), Integer.parseInt(contents[1])-1, Integer.parseInt(contents[2]));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }

    /**
     * Check to see if the draft is complete
     * @return whether or not the draft is complete
     */
    public boolean finished(){
        return pickNumber>totalPicks;
    }

    public List<Team> getTeams() { return teams; }
}
