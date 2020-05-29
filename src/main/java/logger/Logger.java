package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static Logger logger;

    private Logger() {
        // Create the log file
//        File logFile = new File("log.txt");
//        if (logFile.exists()) {
//            logFile.delete();
//        }
//        try {
//            logFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    //create logger if one doesn't exist
    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void logWebScrape(int limit) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String stamp = dateFormat.format(date);
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
