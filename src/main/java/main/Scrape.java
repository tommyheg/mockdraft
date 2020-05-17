package main;

import controllers.ChoiceDecider;
import controllers.Suggestor;
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


    public static void main(String[] args){

        Site site = promptSite();
        ScoreType scoreType = promptScoreType();    //eventually scoretype will be obsolete here because we will use all 3. but not now
        DataType dataType = promptDataType();
        DataStorer dataStorer = new DataStorerFactory().getDataStorer(site, scoreType, dataType);
        int limit = 50;
        dataStorer.storeData(limit);
        dataStorer.copyData();
//        logger.logWebScrape(limit);
        Suggestor suggestor = new Suggestor();
    }
}
