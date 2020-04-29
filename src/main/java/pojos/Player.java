package pojos;

public class Player {

    private int rank;
    private String name, position;
    private String team;


    public Player(int rank, String name, String position, String team) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.team = team;
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

}
