package main;

import controllers.ChoiceDecider;
import controllers.Controller;
import data.DataType;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.cpu.Difficulty;
import webscraping.Site;

import java.util.List;
import java.util.Scanner;

public class CommandLine {

    private static Controller controller;
    private static final ChoiceDecider choiceDecider = new ChoiceDecider();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Ask the user what site to get the data from
     * @return site to get data
     */
    private static Site promptSite(){
        System.out.println("\nWhich website would you like the ADP data from?");
        System.out.println("1. FantasyPros");
        System.out.println("2. ESPN");
        String response = scanner.next();
        while(!choiceDecider.validSite(response,2)){
            System.out.println("Must choose one of the options presented.");
            response = scanner.next();
        }
        int choice = Integer.parseInt(response);
        switch(choice){
            case 1: return Site.FANTASYPROS;
            case 2: return Site.ESPN;
            default: return null;
        }
    }

    /**
     * Ask the user what type of scoring they want
     * @return scoring type
     */
    private static ScoreType promptScoreType(){
        System.out.println("\nWhich scoring type is this draft?");
        System.out.println("1. Standard");
        System.out.println("2. Half-PPR");
        System.out.println("3. PPR");
        String response = scanner.next();
        while(!choiceDecider.validScoreType(response)){
            System.out.println("Must choose one of the options presented.");
            response = scanner.next();
        }
        int choice = Integer.parseInt(response);
        switch (choice){
            case 1: return ScoreType.STANDARD;
            case 2: return ScoreType.HALF;
            case 3: return ScoreType.PPR;
            default: return null;
        }
    }

    /**
     * Get what data type the players should be stored in
     * This might not be worth doing.
     * @return data type
     */
    private static DataType promptDataType(){
        //just a stub for now
        //thinking we could use sql, json, or excel data types
        String response = scanner.next();
        choiceDecider.validDataType(response, 3);
        return DataType.SQL;
    }

    /**
     * Get the size of the league from the user
     * @return size of the league
     */
    private static int promptLeagueSize(){
        System.out.println("How large is your league? 8, 10, or 12?");
        String response = scanner.next();
        while(!choiceDecider.validLeagueSize(response)){
            System.out.println("Must choose a valid league size (8, 10, or 12).");
            response = scanner.next();
        }
        return Integer.parseInt(response);
    }

    /**
     * Get the pick that the user wants to have
     * @param leagueSize- size of the league
     * @return the user's pick
     */
    private static int promptUserPick(int leagueSize){
        System.out.println("Which pick would you like in a "+leagueSize+" team draft?");
        String response = scanner.next();
        while(!choiceDecider.validUserPick(response, leagueSize)){
            System.out.println("Must enter a valid pick.");
            response = scanner.next();
        }
        return Integer.parseInt(response);
    }

    /**
     * Get the CPU difficulty
     * @return difficulty of CPU
     */
    private static Difficulty promptCPUDifficulty(){
        System.out.println("How difficult would you like the CPU to be?");
        System.out.println("1. Stupid");
        System.out.println("2. Random");
        System.out.println("3. Smart");
        String response = scanner.next();
        while(!choiceDecider.validCPUDifficulty(response, 3)){
            System.out.println("Must choose a valid difficulty.");
            response = scanner.next();
        }
        int choice = Integer.parseInt(response);
        switch (choice){
            case 1: return Difficulty.STUPID;
            case 2: return Difficulty.RANDOM;
            case 3: return Difficulty.SMART;
            default: return null;
        }
    }

    private static void presentPlayers(List<Player> players){
        for(int i=0;i<players.size();i++){
            System.out.println(i+": "+players.get(i));
        }
    }

    private static Player userDraft(){
        presentPlayers(controller.nextAvailablePlayers(10));
        System.out.println("Enter the name of the player you want: ");
        String name = scanner.nextLine();
        Player player = controller.draftPlayer(name);
        while(player == null){
            System.out.println("Must choose an available player. Try again");
            name = scanner.nextLine();
            player = controller.draftPlayer(name);
        }
        return player;
    }

    private static Player cpuDraft(){
        return controller.draftPlayerCPU();
    }

    private static Player draftPlayer(){
        if(controller.userTurn()){
            return userDraft();
        } else{
            return cpuDraft();
        }
    }

    /**
     * Begin the draft
     */
    private static void draft(){
        while(!controller.finished()){
            Player player = draftPlayer();
            while(!controller.draft(player)){
                System.out.println("Must choose an available player.");
                player = draftPlayer();
            }
        }
    }

    public static void main(String[] args){

        Site site = promptSite();
        ScoreType scoreType = promptScoreType();
        DataType dataType = promptDataType();
        int leagueSize = promptLeagueSize();
        int userPick = promptUserPick(leagueSize);
        Difficulty difficulty = promptCPUDifficulty();

        controller = new Controller(site, scoreType, dataType, leagueSize, userPick, difficulty);

        controller.setData();

        draft();
    }
}
