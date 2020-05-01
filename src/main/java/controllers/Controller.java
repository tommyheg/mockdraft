package controllers;

import data.DataType;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
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
    private int leagueSize, rounds, totalPicks, pickNumber = 0;
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

    public void draft(){

    }

    private void initializeTeams(Difficulty difficulty, int userPick){
        this.teams = new ArrayList<Team>(leagueSize);
        for(int i=0;i<leagueSize;i++){
            teams.add(new CPUTeamFactory().getCPUTeam(i+1, difficulty));
        }
        teams.set(userPick, new UserTeam(userPick));
    }

    public boolean finished(){
        return pickNumber>totalPicks;
    }

    public void setData(){
        dataStorer.storeData();
    }
}
