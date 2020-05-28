package data.getters;

import pojos.Player;
import pojos.ScoreType;

import java.util.HashMap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalGetter extends DataGetter {

    private List<Player> availablePlayers;
    private Map<String, Player> playerMap;
    private Statement statement;
    private Connection connection;
    private String table;

    public LocalGetter(ScoreType scoreType){
        establishConnection();
        switch(scoreType){
            case STANDARD: table = "standardPlayers"; break;
            case HALF: table = "halfPlayers"; break;
            case PPR: table = "pprPlayers"; break;
        }
        try {
            this.statement = this.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        populate();
    }

    /**
     * Populate the players list and map
     * in the constructor by getting the
     * whole list from server
     */
    private void populate(){
        availablePlayers = getAllPlayers();
        playerMap = new HashMap<>();
        for(Player p: availablePlayers){
            playerMap.put(p.getName().toLowerCase(), p);
        }
    }

    /**
     * Gets all of the players from SQL table and adds them to a list
     * @return List of players from SQL table
     */
    @Override
    public List<Player> getAllPlayers() {
        if(connection == null) establishConnection();
        String s = "select * from "+table;
        List<Player> players = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(s);
            if (!rs.isBeforeFirst()) return null;
            while(rs.next()){
                int rank = rs.getInt(1);
                String lastName = rs.getString(2);
                String firstName = rs.getString(3);
                String fullName = firstName+" "+lastName;
                String position = rs.getString(4);
                String team = rs.getString(5);
                Map<String, Double> projections = new HashMap<>();
                List<String> keys = new Player().getKeys();
                for(int i = 0; i < keys.size(); i++){
                    String temp = rs.getString(i+6);
                    if(temp == null){
                        projections.put(keys.get(i), null);
                    }
                    else {
                        projections.put(keys.get(i), Double.parseDouble(temp.strip()));
                    }
                }

                players.add(new Player(rank, fullName, position, team, projections,rs.getDouble(19),rs.getDouble(20))); //fix projections and ADP

            }
        }
         catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return players;
    }

    /**
     * Get a list of the next N available players
     * through streams
     * @param limit- number of players to get
     * @return a list of available players
     */
    @Override
    public List<Player> nextAvailablePlayers(int limit) {
        return availablePlayers.stream()
                .filter(s->s.getTeamNum() == -1)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Remove a player from the database by
     * setting his team variable to his current team
     * @param player- player to be removed
     * @param teamNum- team he was drafted to
     */
    @Override
    public void removePlayer(Player player, int teamNum) {
        player.setTeamNum(teamNum);
    }

    /**
     * Get a player by his name
     * @param name- name of the player
     * @return the corresponding player
     */
    @Override
    public Player getPlayer(String name) {
        return playerMap.get(name.toLowerCase().replace(".","").replace("'",""));
    }

    /**
     * Get a random available player
     * @param range- range of players to get from
     * @return null if the player is already taken, otherwise the player
     */
    @Override
    public Player getRandomPlayer(int range) {
        int random = (int) (Math.random()*range) + 1;
        Player player = availablePlayers.get(random);
        if(player.getTeamNum() != -1) return null;
        return availablePlayers.get(random);
    }

    /**
     * Get the Nth player
     * @param count- the Nth position
     * @return the Nth player
     */
    @Override
    public Player getNextPlayer(int count) {
        if(count == availablePlayers.size()) return null;
        return availablePlayers.get(count);
    }

    /**
     * connects to sql table
     */
    private void establishConnection(){
        //this is just a local connection but it could eventually be to a server or something
        String url = "jdbc:mysql://localhost:3306/mockdraft?useTimezone=true&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp(){
        try {
            if(connection!=null) connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
