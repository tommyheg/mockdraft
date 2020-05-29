package main;

import data.DataType;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
import logger.Logger;
import pojos.ScoreType;

public class Scrape {

    private static final Logger logger = Logger.getLogger();

    //store all of the players from ffc and fantasypros
    private static void storeData(ScoreType scoreType, int limit) {
        DataStorer dataStorer = new DataStorerFactory().getDataStorer(scoreType, DataType.SQL, 12);
        dataStorer.storePlayers(limit);
        dataStorer.updatePlayers(limit);
        logger.logWebScrape(limit);
    }

    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();
        //limit will be removed once we are done. small for debugging purposes
        int limit = 230;
        for (ScoreType st : ScoreType.values()) {
            System.out.println("Starting ScoreType " + st + "...");
            storeData(st, limit);
        }

        long time = System.currentTimeMillis() - t1;
        time /= 1000;
        System.out.println("Time taken: " + time);
    }
}
