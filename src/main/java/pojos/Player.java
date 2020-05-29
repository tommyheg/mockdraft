package pojos;

import java.util.*;

public class Player {

    private final int RANK;
    private final String POSITION, TEAM;
    private String lastName, firstName, name;
    private Map<String, Double> projections;
    private final List<String> KEYS;
    private double adp, sdev;
    private int teamNum = -1, roundNum = -1, pickNum = -1, selection = -1;
    private double value = -1;

    public Player(int RANK, String name, String POSITION, String TEAM, Map<String, Double> projections, double adp, double sdev) {
        this(RANK, name, POSITION, TEAM, projections);
        this.adp = adp;
        this.sdev = sdev;
    }

    public Player(int RANK, String name, String POSITION, String TEAM, double adp, double sdev) {
        this(RANK, name, POSITION, TEAM, null);
        this.adp = adp;
        this.sdev = sdev;
    }

    public Player(int RANK, String name, String POSITION, String TEAM, Map<String, Double> projections) {
        this.RANK = RANK;
        this.name = name;
        this.POSITION = POSITION;
        this.TEAM = TEAM;
        this.adp = -1;
        this.sdev = -1;
        this.KEYS = new ArrayList<>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(projections);
        setName();
        updateName();
    }

    public Player() {
        this.RANK = -1;
        this.name = "Sentinel Name";
        this.POSITION = "";
        this.TEAM = "";
        this.KEYS = new ArrayList<>();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        setProjections(null);
        this.adp = -1;
        this.sdev = -1;
    }

    private void setProjections(Map<String, Double> projections) {
        //set every projection to nothing
        initializeProjections();

        //update player's projections
        if (projections == null) return;
        for (String stat : projections.keySet()) {
            this.projections.put(stat, projections.get(stat));
        }
    }

    public void initializeProjections() {
        KEYS.add("Points");
        KEYS.add("Rush Att");
        KEYS.add("Rush Yds");
        KEYS.add("Rush Tds");
        KEYS.add("Recs");
        KEYS.add("Rec Yds");
        KEYS.add("Rec Tds");
        KEYS.add("Pass Cmp");
        KEYS.add("Pass Att");
        KEYS.add("Pass Yds");
        KEYS.add("Pass Tds");
        KEYS.add("Pass Ints");
        KEYS.add("Fumbles");

        projections = new HashMap<>();

        //initialize all values to 0
        Double val = 0.0;
        for (String s : KEYS) {
            projections.put(s, val);
        }
    }

    private void updateName() {
        firstName = firstName.replaceAll("\\.", "");
        firstName = firstName.replaceAll("'", "");
        lastName = lastName.replaceAll("\\.", "");
        lastName = lastName.replaceAll("'", "");
        name = firstName + " " + lastName;
    }

    public String toString() {
        return RANK + ": " + name + ", " + TEAM + ", " + POSITION;
    }

    public int getRank() {
        return RANK;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPosition() {
        return POSITION;
    }

    public String getTeam() {
        return TEAM;
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
                POSITION.equals(player.POSITION) &&
                lastName.equals(player.lastName) &&
                firstName.equals(player.firstName) &&
                TEAM.equals(player.TEAM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, POSITION, lastName, firstName, TEAM);
    }

    public double getADP() {
        return adp;
    }

    public double getSDEV() {
        return sdev;
    }

    private void setName() {
        if (lastName.equals("Mahomes")) firstName = "Pat";
    }

    public void setTeamNum(int teamNum) {
        this.teamNum = teamNum;
    }

    public int getTeamNum() {
        return teamNum;
    }

    public void setPick(int round, int pick, int selection) {
        pickNum = pick;
        roundNum = round;
        this.selection = selection;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public int getSelection() {
        return selection;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public int getPickNum() {
        return pickNum;
    }

    public String finishedString() {
        return "Round " + roundNum + ", Pick " + pickNum + ": " + name + ", " + POSITION + ", " + TEAM;
    }

    public List<String> getKEYS() {
        return KEYS;
    }
}
