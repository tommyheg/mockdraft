package pojos;

import java.util.Map;

public class Player {

    private int rank;
    private String name, position;
    private String team;
    private Map<String, Double> projections;


    public Player(int rank, String name, String position, String team, Map<String, Double> projections) {
        this(rank, name, position, team);
        this.projections = projections;
    }

    public Player(int rank, String name, String position, String team) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.team = team;
    }

    public void addProjection(String stat, Double value) {
        projections.put(stat, value);
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
