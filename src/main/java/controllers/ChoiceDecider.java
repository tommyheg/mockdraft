package controllers;

public class ChoiceDecider {


    /**
     * Check to see if a Site response is valid (both an integer and within range)
     * @param response- the option the user selected
     * @param options- number of options of prompt
     * @return whether the response was valid or not
     */
    public boolean validSite(String response, int options){
        int choice;
        try{
            choice = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return false;
        }
        return choice>0 && choice<=options;
    }

    /**
     * Check to see if a ScoreType response is valid (both an integer and within range)
     * @param response- the option the user selected
     * @return whether the response was valid or not
     */
    public boolean validScoreType(String response){
        int choice;
        try{
            choice = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return false;
        }
        return choice>0 && choice<4;
    }

    /**
     * Check to see if a DataType response is valid (both an integer and within range)
     * @param response- the option the user selected
     * @param options- number of options of prompt
     * @return whether the response was valid or not
     */
    public boolean validDataType(String response, int options){
        //just a stub for now
        return true;
    }

    /**
     * Check to see if a leagueSize response is valid (both an integer and one of the supported league sizes)
     * @param response- the league size that the user entered
     * @return whether the response was valid or not
     */
    public boolean validLeagueSize(String response){
        int size;
        try{
            size = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return false;
        }
        return size==8 || size==10 || size==12; //subject to change
    }

    /**
     * Check to see if a userPick response is valid (both an integer and within league size)
     * @param response- the user pick that the user entered
     * @param leagueSize- the size of the league
     * @return whether the response was valid or not
     */
    public boolean validUserPick(String response, int leagueSize){
        int pick;
        try{
            pick = Integer.parseInt(response);
        } catch (NumberFormatException e){
            return false;
        }
        return pick>0 && pick<=leagueSize;
    }

    /**
     * Check to see if a CPUDifficulty response is valid (both an integer and within range)
     * @param response- the option the user selected
     * @param options- number of options of prompt
     * @return whether the response was valid or not
     */
    public boolean validCPUDifficulty(String response, int options){
        int choice;
        try{
            choice = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return false;
        }
        return choice>0 && choice<=options;
    }

    public boolean validSuggestions(String response){
        int choice;
        try{
            choice = Integer.parseInt(response);
        } catch(NumberFormatException e){
            return false;
        }
        return choice == 1 || choice == 2;
    }

}
