package main;

import controllers.ChoiceDecider;
import controllers.Controller;
import controllers.Suggestor;
import data.DataType;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.Team;
import pojos.teams.cpu.Difficulty;
import webscraping.Site;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommandLine {

    private static Controller controller;
    private static final ChoiceDecider choiceDecider = new ChoiceDecider();
    private static final Scanner scanner = new Scanner(System.in);
    private static Suggestor suggestor;
    private static boolean suggestions = false;

    /**
     * Ask the user what type of scoring they want
     * @return scoring type
     */
    private static ScoreType promptScoreType(){
        System.out.println("\nWhich scoring type is this draft?");
        System.out.println("1. Standard");
        System.out.println("2. Half-PPR");
        System.out.println("3. PPR");
//        String response = scanner.next();
        String response = "1";
        while(!choiceDecider.validScoreType(response)){
            System.out.println("Must choose one of the options presented.");
            response = scanner.next();
        }
        int choice = Integer.parseInt(response);
        switch (choice){
            case 1: return ScoreType.STANDARD;
            case 2: return ScoreType.HALF;
            default: return ScoreType.PPR;
        }
    }

    /**
     * Get the size of the league from the user
     * @return size of the league
     */
    private static int promptLeagueSize(){
        System.out.println("\nHow large is your league? 8, 10, or 12?");
//        String response = scanner.next();
        String response = "12";
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
        System.out.println("\nWhich pick would you like in a "+leagueSize+" team draft?");
//        String response = scanner.next();
        String response = "2";
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
        System.out.println("\nHow difficult would you like the CPU to be?");
        System.out.println("1. Stupid");
        System.out.println("2. Random");
        System.out.println("3. Smart");
//        String response = scanner.next();
        String response = "1";
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

    /**
     * Ask whether the user wants suggestions or not
     * @return whether they want suggestions or not
     */
    private static boolean promptSuggestions(){
        System.out.println("Would you like suggestions for your picks?");
        System.out.println("1 for yes, 2 for no.");
//        String response = scanner.next();
        String response = "2";
        while(!choiceDecider.validSuggestions(response)){
            System.out.println("Must enter 1 for yes or 2 for no.");
            response = scanner.next();
        }
        return Integer.parseInt(response) == 1;
    }

    /**
     * Present the next few players for the user
     * @param players- list of players
     */
    private static void presentPlayers(List<Player> players){
        for (Player player : players) {
            System.out.println(player);
        }
    }

    /**
     * Get the player selection from the user
     * @return the player that the user selected
     */
    private static Player userDraft(){
        if(suggestions){
            Map<String, Double> suggestions = controller.getSuggestions();
            List<String> sorted = controller.sortSuggestions(suggestions);
            System.out.println("Here are the player suggestions: ");
            for(String s: sorted){
                System.out.println(s+": "+suggestions.get(s));
            }
        } else presentPlayers(controller.nextAvailablePlayers(10));
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

    /**
     * Get the player the cpu drafts
     * @return the player the cpu drafts
     */
    private static Player cpuDraft(){
        return controller.draftPlayerCPU();
    }

    /**
     * Get the player that is drafted
     * @return the player that is drafted
     */
    private static Player draftPlayer(){
        if(controller.userTurn()){
            return userDraft();
        } else{
            return cpuDraft();
        }
    }

    /**
     * Print out all of the teams
     */
    private static void presentTeams(){
        List<Team> teams = controller.getTeams();

        for(Team team: teams){
            System.out.println();
            if(team.isUser()){
                System.out.println("-----USER TEAM-----");
            }
            System.out.println(team);
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

        suggestions = promptSuggestions();
        ScoreType scoreType = promptScoreType();
        int leagueSize = promptLeagueSize();
        int userPick = promptUserPick(leagueSize);
        Difficulty difficulty = promptCPUDifficulty();

        suggestor = new Suggestor(scoreType);
        controller = new Controller(scoreType, leagueSize, userPick, difficulty);

        draft();

        presentTeams();

        controller.cleanUp();

    }
}
