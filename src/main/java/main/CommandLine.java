package main;

import controllers.Controller;
import data.DataType;
import pojos.ScoreType;
import webscraping.Site;

public class CommandLine {

    private static Site getSite(){
        return Site.FANTASYPROS;
    }

    private static ScoreType getScoreType(){
        return ScoreType.STANDARD;
    }

    private static DataType getDataType(){
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
