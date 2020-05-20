package main;

import controllers.ChoiceDecider;
import data.DataType;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
import logger.Logger;
import pojos.ScoreType;
import webscraping.Site;

import java.util.Scanner;

public class Scrape {

    private static ChoiceDecider choiceDecider = new ChoiceDecider();
    private static Scanner scanner = new Scanner(System.in);
    private static Logger logger = Logger.getLogger();

    /**
     * Ask what site to get the data from
     * @return site to get data
     */
    private static Site promptSite(){
        System.out.println("\nWhich website would you like the ADP data from?");
        System.out.println("1. FantasyPros");
        System.out.println("2. ESPN");
        System.out.println("3. FantasyFootball Calculator");
//        String response = scanner.next();
        String response = "3";
        while(!choiceDecider.validSite(response,3)){
            System.out.println("Must choose one of the options presented.");
            response = scanner.next();
        }
        int choice = Integer.parseInt(response);
        switch(choice){
            case 1: return Site.FANTASYPROS;
            case 2: return Site.ESPN;
            case 3: return Site.FFCALCULATOR;
            default: return null;
        }
    }

    /**
     * Ask what type of scoring they want
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
//        String response = scanner.next();
        String response = "1";
        choiceDecider.validDataType(response, 3);
        return DataType.SQL;
    }

    /**
     * Store for the given score type and league size.
     * Right now we are only storing data with sql.
     * @param scoreType- Standard, Half, or PPR scoring type. Determines the table
     * @param dataType- how the data will be stored (just sql for now)
     * @param leagueSize- how big the league is (for scraping)
     * @param limit- how many players to get (this will be eliminated later)
     */
    private static void storeData(ScoreType scoreType, DataType dataType, int leagueSize, int limit){
        DataStorer dataStorer = new DataStorerFactory().getDataStorer(scoreType, dataType, leagueSize);
        dataStorer.storeData(limit);
        dataStorer.updateData(limit);
//        dataStorer.createCopy();
        logger.logWebScrape(limit);
    }

    public static void main(String[] args){

        long t1 = System.currentTimeMillis();
        //limit will be removed once we are done. small for debugging purposes
        //league size will just be 10 (i don't think that the data varies too much from size to size)
            //this simplifies our tables. only 6 needed instead of 18
        int limit = 420;
        for(ScoreType st: ScoreType.values()){
            System.out.println("Starting ScoreType "+st+"...");
            storeData(st, DataType.SQL, 12, limit);
        }
        long time = System.currentTimeMillis() - t1;
        time/=1000;
        System.out.println("Time taken: "+time);
    }
}
