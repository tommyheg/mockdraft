package controllers;

public class ChoiceDecider {

    public boolean invalidScoreType(String response){
        int choice;
        try{
            choice = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return true;
        }
        return choice <= 0 || choice >= 4;
    }

    public boolean invalidLeagueSize(String response){
        int size;
        try{
            size = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return true;
        }
        return size != 8 && size != 10 && size != 12;
    }


    public boolean invalidUserPick(String response, int leagueSize){
        int pick;
        try{
            pick = Integer.parseInt(response);
        } catch (NumberFormatException e){
            return true;
        }
        return pick <= 0 || pick > leagueSize;
    }

    public boolean invalidCPUDifficulty(String response, int options){
        int choice;
        try{
            choice = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return true;
        }
        return choice <= 0 || choice > options;
    }

    public boolean invalidSuggestions(String response){
        int choice;
        try{
            choice = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return true;
        }
        return choice != 1 && choice != 2;
    }

}
