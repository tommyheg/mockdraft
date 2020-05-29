package controllers;

import data.DataType;
import data.getters.DataGetter;
import data.getters.LocalGetter;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.Team;
import pojos.teams.UserTeam;
import pojos.teams.cpu.CPUTeam;
import pojos.teams.cpu.CPUTeamFactory;
import pojos.teams.cpu.Difficulty;

import java.util.*;

public class Controller {

    private final DataGetter dataGetter;
    private final Suggestor suggestor;
    private final DataStorer dataStorer;
    private final int leagueSize;
    private final int totalPicks;
    private int pickNumber = 1, currentPick = 1, currentRound = 1, roundPick = 1;
    private List<Team> teams;
    private Team currentTeam, userTeam;
    private boolean started;

    public Controller(ScoreType scoreType, int leagueSize, int userPick, Difficulty difficulty) {
        int rounds = 16;
        this.leagueSize = leagueSize;
        this.totalPicks = rounds * leagueSize;
        initializeTeams(difficulty, userPick);
        this.currentTeam = teams.get(0);
        this.suggestor = new Suggestor(scoreType, userPick, leagueSize);
        this.dataStorer = new DataStorerFactory().getDataStorer(scoreType, DataType.SQL, leagueSize);
        this.dataGetter = new LocalGetter(scoreType);
    }

    public List<Player> getSuggestions() {
        List<Player> suggestions = suggestor.getSuggestions(dataGetter, currentRound, pickNumber);
        suggestions.sort((player, t1) -> (int) (player.getValue() * 1000 - t1.getValue() * 1000));
        return suggestions;
    }

    //draft a player to the current team and remove him (set his team and take off database)
    public boolean draft(Player player) {
        if (!currentTeam.addPlayer(player)) {
            return false;
        }
        if (!started) started = true;
        player.setPick(currentRound, roundPick, pickNumber);
        removePlayer(player, currentPick - 1);
        advanceTurn();
        return true;
    }

    //return the player from the database if he is available
    public Player draftPlayer(String name) {
        if (name.split(" ").length < 2) return null;
        Player player = dataGetter.getPlayer(name);
        if (player == null) return null;
        if (player.getTeamNum() == -1) return player;
        else return null;
    }


    public Player draftPlayerCPU() {
        return ((CPUTeam) currentTeam).selectPlayer(dataGetter);
    }

    private void removePlayer(Player player, int teamNum) {
        dataGetter.removePlayer(player, teamNum);
    }

    public void advanceTurn() {
        pickNumber += 1;

        if (currentRound % 2 == 1) {
            currentPick += 1;
            if (currentPick > leagueSize) currentPick -= 1;
        } else {
            currentPick -= 1;
            if (currentPick == 0) currentPick += 1;
        }

        currentRound = (pickNumber - 1) / leagueSize + 1;
        roundPick = currentRound % 2 == 0 ? (leagueSize + 1) - currentPick : currentPick;
        currentTeam = teams.get(currentPick - 1);
    }

    public boolean isUserTurn() {
        return currentTeam.isUser();
    }

    public List<Player> nextAvailablePlayers(int limit) {
        return dataGetter.nextAvailablePlayers(limit);
    }

    public List<Player> nextAvailablePlayers() {
        return dataGetter.nextAvailablePlayers(250);
    }

    private void initializeTeams(Difficulty difficulty, int userPick) {
        this.teams = new ArrayList<>(leagueSize);
        for (int i = 0; i < leagueSize; i++) {
            teams.add(new CPUTeamFactory().getCPUTeam(i + 1, difficulty));
        }
        teams.set(userPick - 1, new UserTeam(userPick, leagueSize));
        userTeam = teams.get(userPick - 1);
    }

    public boolean draftComplete() {
        return pickNumber > totalPicks;
    }

    public boolean draftStarted() {
        return started;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public int getRound() {
        return currentRound;
    }

    public int getCurrentPick() {
        return roundPick;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public Team getUserTeam() {
        return userTeam;
    }

    public int getPickNumber() {
        return pickNumber;
    }

    public void cleanUp() {
        dataStorer.cleanUp();
        dataGetter.cleanUp();
    }

}
