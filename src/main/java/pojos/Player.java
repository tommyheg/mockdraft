package pojos;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final int rank;
    private final String name, position;
    private final String team;
    private Map<String, Double> projections;

    public Player(int rank, String name, String position, String team, Map<String, Double> projections) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.team = team;
        setProjections(projections);
    }

    private void setProjections(Map<String, Double> pros){
        //set every projection to nothing
        projections = new HashMap<String, Double>();

        projections.put("RUSH ATT", 0.0);
        projections.put("RUSH YDS", 0.0);
        projections.put("RUSH TDS", 0.0);
        projections.put("RECS", 0.0);
        projections.put("REC YDS", 0.0);
        projections.put("REC TDS", 0.0);
        projections.put("FUMBLES", 0.0);
        projections.put("POINTS", 0.0);
        projections.put("PASS CMP", 0.0);
        projections.put("PASS ATT", 0.0);
        projections.put("PASS YDS", 0.0);
        projections.put("PASS TDS", 0.0);
        projections.put("PASS INTS", 0.0);

        for(String stat: pros.keySet()){
            projections.put(stat, pros.get(stat));
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
