package logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static Logger logger;

    private Logger() {

        // Create the log file
        File logFile = new File("log.txt");
        if (logFile.exists()) {
            logFile.delete();
        }
        try {
            logFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * singleton implementation of a logger.
     * check to see if a logger exists before creating one
     * @return a Logger
     */
    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    /**
     * log either the user input or results from tournament in a log file
     * @param message- message that needs to be logged
     */
    public void log(String message) {
        try {
            FileWriter logWriter = new FileWriter("log.txt", true);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            logWriter.append(currentTime + ": " + message + "\n");
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logWebScrape(int limit){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String stamp = dateFormat.format(date);
        System.out.println(stamp);
        try {
            FileWriter logWriter = new FileWriter("web_scraping.txt");
            logWriter.append(stamp);
            logWriter.append("\n");
            logWriter.append(Integer.toString(limit));
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
