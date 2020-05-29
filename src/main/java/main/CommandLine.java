package main;

import controllers.ChoiceDecider;
import controllers.Controller;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.Team;
import pojos.teams.cpu.Difficulty;

import java.util.List;
import java.util.Scanner;

public class CommandLine {

    private static Controller controller;
    private static final ChoiceDecider choiceDecider = new ChoiceDecider();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean suggestions = false;

    //get the scoring type from the user
    private static ScoreType promptScoreType() {
        System.out.println("\nWhich scoring type is this draft?");
        System.out.println("1. Standard");
        System.out.println("2. Half-PPR");
        System.out.println("3. PPR");
//        String response = scanner.next();
        String response = "2";  //keep it at half for now
        while (choiceDecider.invalidScoreType(response)) {
            System.out.println("Must choose one of the options presented.");
            response = scanner.next();
        }
        int choice = Integer.parseInt(response);
        switch (choice) {
            case 1:
                return ScoreType.STANDARD;
            case 2:
                return ScoreType.HALF;
            default:
                return ScoreType.PPR;
        }
    }

    //get the league size from the user
    private static int promptLeagueSize() {
        System.out.println("\nHow large is your league? 8, 10, or 12?");
//        String response = scanner.next();
        String response = "10"; //keep it at 10 for now
        while (choiceDecider.invalidLeagueSize(response)) {
            System.out.println("Must choose a valid league size (8, 10, or 12).");
            response = scanner.next();
        }
        return Integer.parseInt(response);
    }

    //get the user pick from the user
    private static int promptUserPick(int leagueSize) {
        System.out.println("\nWhich pick would you like in a " + leagueSize + " team draft?");
//        String response = scanner.next();
        String response = "5";  //keep it at 5 for now
        while (choiceDecider.invalidUserPick(response, leagueSize)) {
            System.out.println("Must enter a valid pick.");
            response = scanner.next();
        }
        return Integer.parseInt(response);
    }

    //get the cpu difficulty from the user
    //eventually remove this once the cpu is 'realistic'
    private static Difficulty promptCPUDifficulty() {
        System.out.println("\nHow difficult would you like the CPU to be?");
        System.out.println("1. Stupid");
        System.out.println("2. Random");
        System.out.println("3. Smart");
//        String response = scanner.next();
        String response = "1";  //keep at stupid for now
        while (choiceDecider.invalidCPUDifficulty(response, 3)) {
            System.out.println("Must choose a valid difficulty.");
            response = scanner.next();
        }
        int choice = Integer.parseInt(response);
        switch (choice) {
            case 1:
                return Difficulty.STUPID;
            case 2:
                return Difficulty.RANDOM;
            default:
                return Difficulty.SMART;
        }
    }

    //ask if the user want suggestions
    private static boolean promptSuggestions() {
        System.out.println("Would you like suggestions for your picks?");
        System.out.println("1 for yes, 2 for no.");
//        String response = scanner.next();
        String response = "1";  //keep at 1 for now
        while (choiceDecider.invalidSuggestions(response)) {
            System.out.println("Must enter 1 for yes or 2 for no.");
            response = scanner.next();
        }
        return Integer.parseInt(response) == 1;
    }

    //present the next 10 players for the user
    private static void presentPlayers(List<Player> players) {
        for (Player player : players) {
            System.out.println(player);
        }
    }

    //print out suggestions and get the user's selection
    private static Player userDraftPlayer() {
        System.out.println("\nRound " + controller.getRound() + ", Pick " + controller.getCurrentPick() + ": ");
        //present players and suggestions
        presentPlayers(controller.nextAvailablePlayers(10));
        if (suggestions) {
            List<Player> sorted = controller.getSuggestions();
            System.out.println("Here are the player suggestions: ");
            for (Player s : sorted) {
                System.out.println(s.getName() + ": " + s.getValue());
            }
        }

        System.out.println("Enter the name of the player you want: ");
        String name = scanner.nextLine();
        Player player = controller.draftPlayer(name);
        while (player == null) {
            System.out.println("Must choose an available player. Try again");
            name = scanner.nextLine();
            player = controller.draftPlayer(name);
        }
        return player;
    }

    private static Player draftPlayer() {
//        if (controller.isUserTurn()) {
//            return userDraft();
//        } else {
//            return cpuDraft();
//        }
        return controller.isUserTurn() ? userDraftPlayer() : controller.draftPlayerCPU();
    }

    private static void presentTeams() {
        List<Team> teams = controller.getTeams();

        for (Team team : teams) {
            System.out.println();
            if (team.isUser()) {
                System.out.println("-----USER TEAM-----");
            }
            System.out.println(team);
        }
    }

    //draft players until the draft is over
    private static void draft(int size, ScoreType scoreType) {
        System.out.println("\nThe " + size + " team " + scoreType.toString().toLowerCase() + " scoring type draft is beginning:");
        while (!controller.draftComplete()) {
            Player player = draftPlayer();
            while (!controller.draft(player)) {
                System.out.println("Must choose an available player.");
                player = draftPlayer();
            }
        }
    }

    public static void main(String[] args) {

        suggestions = promptSuggestions();
        ScoreType scoreType = promptScoreType();
        int leagueSize = promptLeagueSize();
        int userPick = promptUserPick(leagueSize);
        Difficulty difficulty = promptCPUDifficulty();

        controller = new Controller(scoreType, leagueSize, userPick, difficulty);

        draft(leagueSize, scoreType);

        presentTeams();

        controller.cleanUp();

    }
}
