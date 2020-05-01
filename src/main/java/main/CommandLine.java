package main;

import controllers.Controller;
import data.DataType;
import pojos.ScoreType;
import webscraping.Site;

import java.util.Scanner;

public class CommandLine {

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

    private static void setupDraft(){

    }

    public static void main(String[] args){

        Site site = getSite();
        ScoreType scoreType = getScoreType();
        DataType dataType = getDataType();

        Controller controller = new Controller(site, scoreType, dataType);

        controller.setData();

        setupDraft();
    }
}
