package pojos;

import java.util.*;

public class Player {

    private final int rank;
    private final String position;
    private String lastName, firstName, name;
    private final String team;
    private Map<String, Double> projections;
    private final List<String> keys;
    private final double ADP;
    private final double SDEV;
    private int teamNum = -1, roundNum = -1, pickNum = -1;

    public Player(int rank, String name, String position, String team, Map<String, Double> projections, double adp, double sdev) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.team = team;
        this.ADP = adp;
        this.SDEV = sdev;
        this.keys = new ArrayList<>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(projections);
        setName();
        updateName();
    }

    public Player(int rank, String name, String position, String team, double adp, double sdev) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.team = team;
        this.ADP = adp;
        this.SDEV = sdev;
        this.keys = new ArrayList<>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(null);
        setName();
        updateName();
    }

    public Player(int rank, String name, String position, String team, Map<String, Double> projections) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.team = team;
        this.ADP = -1;
        this.SDEV = -1;
        this.keys = new ArrayList<>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(projections);
        setName();
        updateName();
    }

    public Player(){
        this.rank = 0;
        this.name = "Sentinel Name";
        this.position = "";
        this.team = "";
        this.keys = new ArrayList<>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(null);
        this.ADP = -1;
        this.SDEV = -1;
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

        projections = new HashMap<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name) &&
                position.equals(player.position) &&
                lastName.equals(player.lastName) &&
                firstName.equals(player.firstName) &&
                team.equals(player.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, lastName, firstName, team);
    }

    public double getADP() {
        return ADP;
    }

    public double getSDEV() {
        return SDEV;
    }

    private void updateName(){
        firstName = firstName.replaceAll("\\.", "");
        firstName = firstName.replaceAll("'", "");
        lastName = lastName.replaceAll("\\.", "");
        lastName = lastName.replaceAll("'", "");
        name = firstName + " " + lastName;
    }

    private void setName(){
        if(lastName.equals("Mahomes")) firstName = "Pat";
    }

    public void setTeamNum(int teamNum){
        this.teamNum = teamNum;
    }

    public int getTeamNum(){
        return teamNum;
    }

    public void setPick(int round, int pick){
        this.pickNum = pick;
        this.roundNum = round;
    }

    public String finishedString(){
        return "Round "+roundNum+", Pick "+pickNum+": "+name+", "+position+", "+team;
    }
    public List<String> getKeys() {
        return keys;
    }
}
