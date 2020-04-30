package main;

import controllers.Controller;
import data.DataType;
import pojos.ScoreType;
import webscraping.Site;

public class CommandLine {

    /**
     * Ask the user what site to get the data from
     * @return site to get data
     */
    private static Site getSite(){
        //just a stub for now, will implement espn and other sites later
        return Site.FANTASYPROS;
    }

    /**
     * Ask the user what type of scoring they want
     * @return scoring type
     */
    private static ScoreType getScoreType(){
        //just a stub for now
        return ScoreType.STANDARD;
    }

    /**
     * Get what data type the players should be stored in
     * @return data type
     */
    private static DataType getDataType(){
        //just a stub for now
        //thinking we could use sql, json, or excel data types
        return DataType.SQL;
    }


    public static void main(String[] args){

        Site site = getSite();
        ScoreType scoreType = getScoreType();
        DataType dataType = getDataType();

        Controller controller = new Controller(site, scoreType, dataType);

        controller.storeData();
    }
}
