package data.getters;

import pojos.Player;
import pojos.ScoreType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLGetter extends DataGetter{

    private Connection connection;
    private Statement statement;
    private String table;

    public SQLGetter(ScoreType scoreType){
        switch (scoreType){
            case STANDARD: table = "standardPlayers"; break;
            case HALF: table = "halfPlayers"; break;
            case PPR: table = "pprPlayers"; break;
        }
//        table = "players";  //go thru each method and change 'players' to table
    }

    /**
     * Get the next X player available from the database
     * @param count- the X player available
     * @return the X player
     */
    @Override
    public Player getNextPlayer(int count){
        if(connection == null) establishConnection();
        String s = "select * from "+table+" limit "+count+", 1";
        Player player = null;
        try{
            ResultSet rs = statement.executeQuery(s);
            if(!rs.isBeforeFirst()) return null;
            rs.next();
            int rank = rs.getInt(1);
            String lastName = rs.getString(2);
            String firstName = rs.getString(3);
            String fullName = firstName+" "+lastName;
            String position = rs.getString(4);
            String team = rs.getString(5);
            player = new Player(rank, fullName, position, team, null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return player;
    }

    /**
     * Select one of the next X available players
     * @param range- the range of players to select from
     */
    @Override
    public Player getRandomPlayer(int range){
        int random = (int) (Math.random()*range) + 1;
        return getNextPlayer(random);
    }

    /**
     * Get the player from the sql if he exists, otherwise return null
     * @param name- name of the player to get
     * @return the player from the database
     */
    @Override
    public Player getPlayer(String name) {
        if(connection == null) establishConnection();
        String first = name.split(" ")[0];
        String last = name.split(" ")[1];
        String s = "select * from "+table+" where FullName = \""+name+"\"" +
                " or (FirstName = \""+first+"\" and LastName = \"" +
                last+"\");";
        Player player = null;
        try{
            ResultSet rs = statement.executeQuery(s);
            if(!rs.isBeforeFirst()) return null;
            rs.next();
            int rank = rs.getInt(1);
            String lastName = rs.getString(2);
            String firstName = rs.getString(3);
            String fullName = firstName+" "+lastName;
            String position = rs.getString(4);
            String team = rs.getString(5);
            player = new Player(rank, fullName, position, team, null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return player;
    }

    /**
     * Remove a player from the sql database after he's been drafted
     */
    @Override
    public void removePlayer(Player player){
        if(connection == null) establishConnection();
        String name = player.getName();
        String first = player.getFirstName();
        String last = player.getLastName();
        String s = "delete from "+table+" where FullName = \""+name+"\"" +
                " or (FirstName = \""+first+"\" and LastName = \"" +
                last+"\");";
        try{
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get the next X players from the sql database.
     * This could maybe be optimized (just store a list
     * of available players or something).
     * @param limit- number of players to get
     * @return a list of the next X players
     */
    @Override
    public List<Player> nextAvailablePlayers(int limit){
        List<Player> players = new ArrayList<>();

        Player player = new Player();
        for(int i=0;i<limit && player!=null;i++){
            player = getNextPlayer(i);
            if(player!=null) players.add(player);
        }

        return players;
    }

    /**
     * Establish connection to sql database
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

    /**
     * Close connection to sql database
     */
    private void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}
