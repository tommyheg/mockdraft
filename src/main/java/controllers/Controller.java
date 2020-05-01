package controllers;

import data.DataType;
import data.storage.DataStorer;
import data.storage.DataStorerFactory;
import pojos.ScoreType;
import webscraping.Site;

public class Controller {

    private DataStorer dataStorer;

    public Controller(Site site, ScoreType scoreType, DataType dataType){
        this.dataStorer = new DataStorerFactory().getDataStorer(site, scoreType, dataType);
    }


    public void setData(){
        dataStorer.storeData();
    }
}
