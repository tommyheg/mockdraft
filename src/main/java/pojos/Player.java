package pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    private final int rank;
    private final String name, position;
    private final String lastName, firstName;
    private final String team;
    private Map<String, Double> projections;
    private final List<String> keys;

    public Player(int rank, String name, String position, String team, Map<String, Double> projections) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.team = team;
        this.keys = new ArrayList<String>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(projections);
    }

    public Player(){
        this.rank = 0;
        this.name = "Sentinel Name";
        this.position = "";
        this.team = "";
        this.keys = new ArrayList<String>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(null);
    }

    /**
     * Set the player's projections
     * @param projections- projections to be added
     */
    private void setProjections(Map<String, Double> projections){
        //set every projection to nothing
        initializeProjections();

        //update player's projections
        if(projections == null) return;
        for(String stat: projections.keySet()){
            this.projections.put(stat, projections.get(stat));
        }
    }

    /**
     * Initalize all projections to null so all stats show up in table
     */
    public void initializeProjections(){
        keys.add("Points");
        keys.add("Rush Att");
        keys.add("Rush Yds");
        keys.add("Rush Tds");
        keys.add("Recs");
        keys.add("Rec Yds");
        keys.add("Rec Tds");
        keys.add("Pass Cmp");
        keys.add("Pass Att");
        keys.add("Pass Yds");
        keys.add("Pass Tds");
        keys.add("Pass Ints");
        keys.add("Fumbles");

        projections = new HashMap<String, Double>();

        //initialize all values to null (maybe 0 later- idk)
        Double val = null;
        for(String s: keys){
            projections.put(s, val);
        }
    }

    public String toString() {
        return rank + ": " + name + ", " + team + ", " + position;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getLastName() { return lastName; }

    public String getFirstName() { return firstName; }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }

    public Map<String, Double> getProjections() {
        return projections;
    }
}
