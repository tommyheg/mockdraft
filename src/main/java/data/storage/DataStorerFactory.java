package data.storage;

import data.DataType;
import pojos.ScoreType;
import webscraping.Site;

public class DataStorerFactory {

    public DataStorer getDataStorer(Site site, ScoreType scoreType, DataType dataType){
        if(dataType==DataType.SQL){
            return new SQLStorer(site, scoreType);
        } else if(dataType==DataType.JSON){
            return null;
        } else if(dataType==DataType.EXCEL){
            return null;
        }

        return null;
    }
}
