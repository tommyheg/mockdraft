package data.storage;

import data.DataType;
import pojos.ScoreType;

//we could refactor everything so only sql is available
//i don't see why we would store the players in json or excel
public class DataStorerFactory {

    public DataStorer getDataStorer(ScoreType scoreType, DataType dataType, int leagueSize) {
        if (dataType == DataType.SQL) {
            return new SQLStorer(scoreType, leagueSize);
        } else if (dataType == DataType.JSON) {
            return null;
        } else if (dataType == DataType.EXCEL) {
            return null;
        }

        return null;
    }
}
