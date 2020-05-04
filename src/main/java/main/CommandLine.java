package main;

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

    /**
     * Ask the user what site to get the data from
     * @return site to get data
     */
    private static Site getSite(){
        System.out.println("\nWhich website would you like the ADP data from?");
        System.out.println("1. FantasyPros");
        System.out.println("2. ESPN");
//        int choice = getSiteChoice();
        int choice = 1; //stub for getSiteChoice()
        switch(choice){
            case 1: return Site.FANTASYPROS;
            case 2: return Site.ESPN;
            default: return null;
        }
    }

    /**
     * Keep asking for user's choice until a valid one is selected
     * @return choice the user selected
     */
    private static int getSiteChoice(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        try{
            choice = scanner.nextInt();
        } catch (NumberFormatException e){
            System.out.println("Must choose one of the options presented.");
            choice = getSiteChoice();
        }
        if(choice<1 || choice >2){
            System.out.println("Must choose one of the options presented.");
            choice = getSiteChoice();
        }
        return choice;
    }

    /**
     * Ask the user what type of scoring they want
     * @return scoring type
     */
    private static ScoreType getScoreType(){
        System.out.println("\nWhich scoring type is this draft?");
        System.out.println("1. Standard");
        System.out.println("2. Half-PPR");
        System.out.println("3. PPR");
//        int choice = getScoreTypeChoice();
        int choice = 1; //stub for getScoreTypeChoice()
        switch (choice){
            case 1: return ScoreType.STANDARD;
            case 2: return ScoreType.HALF;
            case 3: return ScoreType.PPR;
            default: return null;
        }
    }

    private static int getScoreTypeChoice(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        try{
            choice = scanner.nextInt();
        } catch (NumberFormatException e){
            System.out.println("Must choose one of the options presented.");
            choice = getSiteChoice();
        }
        if(choice<1 || choice >3){
            System.out.println("Must choose one of the options presented.");
            choice = getSiteChoice();
        }
        return choice;
    }

    /**
     * Get what data type the players should be stored in
     * This might not be worth doing.
     * @return data type
     */
    private static DataType getDataType(){
        //just a stub for now
        //thinking we could use sql, json, or excel data types
        return DataType.SQL;
    }

    /**
     * Get the size of the league from the user
     * @return size of the league
     */
    private static int getLeagueSize(){
        System.out.println("How large is your league? 8, 10, or 12?");
        Scanner scanner = new Scanner(System.in);
        int size;
        try{
//            size = scanner.nextInt();
            size = 10;
        } catch(NumberFormatException e){
            System.out.println("Must enter a valid league size.");
            size = getLeagueSize();
        }
        if(size!=8 && size!=10 && size!=12){
            System.out.println("Must enter a valid league size.");
            size = getLeagueSize();
        }
        return size;
    }

    /**
     * Get the pick that the user wants to have
     * @param leagueSize- size of the league
     * @return the user's pick
     */
    private static int getUserPick(int leagueSize){
        System.out.println("Which pick would you like in a "+leagueSize+" team draft?");
        Scanner scanner = new Scanner(System.in);
        int pick;
        try{
//            pick = scanner.nextInt();
            pick = 5;
        } catch(NumberFormatException e){
            System.out.println("Must enter a valid pick.");
            pick = getUserPick(leagueSize);
        }
        if(pick<1 || pick>leagueSize){
            System.out.println("Must enter a valid pick");
            pick = getUserPick(leagueSize);
        }
        return pick;
    }

    /**
     * Get the CPU difficulty
     * @return difficulty of CPU
     */
    private static Difficulty getCPUDifficulty(){
        System.out.println("How difficult would you like the CPU to be?");
        System.out.println("1. Stupid");
        System.out.println("2. Random");
        System.out.println("3. Smart");
//        int choice = getCPUDifficultyChoice();
        int choice = 1; //stub for getCPUDifficultChoice()
        switch (choice){
            case 1: return Difficulty.STUPID;
            case 2: return Difficulty.RANDOM;
            case 3: return Difficulty.SMART;
            default: return null;
        }
    }

    /**
     * Keep asking for the CPU's difficulty until a valid choice is selected.
     * @return CPU difficulty choice
     */
    private static int getCPUDifficultyChoice(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        try{
            choice = scanner.nextInt();
        } catch (NumberFormatException e){
            System.out.println("Must choose one of the options presented.");
            choice = getSiteChoice();
        }
        if(choice<1 || choice >3){
            System.out.println("Must choose one of the options presented.");
            choice = getSiteChoice();
        }
        return choice;
    }

    private static void presentPlayers(List<Player> players){
        for(int i=0;i<players.size();i++){
            System.out.println(i+": "+players.get(i));
        }
    }

    private static int getPlayerChoice(int size){
        Scanner scanner = new Scanner(System.in);
        int choice;
        try{
            choice = scanner.nextInt();
        } catch (NumberFormatException e){
            System.out.println("Must choose one of the players presented.");
            choice = getSiteChoice();
        }
        if(choice<1 || choice >size){
            System.out.println("Must choose one of the players presented.");
            choice = getSiteChoice();
        }
        return choice;
    }

    private static void draft(){
        while(!controller.finished()){
            if(controller.userTurn()){
                presentPlayers(controller.nextAvailablePlayers(10));
            }
            Player player = controller.draftPlayer();
            while(player==null) {
                System.out.println("Must choose an available player. Try again");
                player = controller.draftPlayer();
            }
            controller.removePlayer(player);
            controller.advanceTurn();
        }
    }

    public static void main(String[] args){

        Site site = getSite();
        ScoreType scoreType = getScoreType();
        DataType dataType = getDataType();
        int leagueSize = getLeagueSize();
        int userPick = getUserPick(leagueSize);
        Difficulty difficulty = getCPUDifficulty();

        controller = new Controller(site, scoreType, dataType, leagueSize, userPick, difficulty);

        controller.setData();

        draft();
    }
}
