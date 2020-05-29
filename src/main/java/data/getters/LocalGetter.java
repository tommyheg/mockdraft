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
    private String table = "standardPlayers";

    public LocalGetter(ScoreType scoreType) {
        establishConnection();
        if (scoreType == ScoreType.HALF) table = "halfPlayers";
        else if (scoreType == ScoreType.PPR) table = "pprPlayers";
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        populatePlayers();
    }

    //populate the local database from the sql server
    private void populatePlayers() {
        availablePlayers = getAllPlayers();
        playerMap = new HashMap<>();
        for (Player p : availablePlayers) {
            playerMap.put(p.getName().toLowerCase(), p);
        }
    }

    //get all the players from the sql server
    @Override
    public List<Player> getAllPlayers() {
        if (connection == null) establishConnection();
        String s = "select * from " + table;
        List<Player> players = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(s);
            if (!rs.isBeforeFirst()) return null;
            while (rs.next()) {
                int rank = rs.getInt(1);
                String lastName = rs.getString(2);
                String firstName = rs.getString(3);
                String fullName = firstName + " " + lastName;
                String position = rs.getString(4);
                String team = rs.getString(5);
                Map<String, Double> projections = new HashMap<>();
                List<String> keys = new Player().getKEYS();
                for (int i = 0; i < keys.size(); i++) {
                    String temp = rs.getString(i + 6);
                    if (temp == null) {
                        projections.put(keys.get(i), 0.0);
                    } else {
                        projections.put(keys.get(i), Double.parseDouble(temp.strip()));
                    }
                }
                players.add(new Player(rank, fullName, position, team, projections, rs.getDouble(19), rs.getDouble(20)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return players;
    }

    //get the next N available players
    @Override
    public List<Player> nextAvailablePlayers(int limit) {
        return availablePlayers.stream()
                .filter(s -> s.getTeamNum() == -1)
                .limit(limit)
                .collect(Collectors.toList());
    }

    //remove a player by validating his team num
    @Override
    public void removePlayer(Player player, int teamNum) {
        player.setTeamNum(teamNum);
    }

    @Override
    public Player getPlayer(String name) {
        return playerMap.get(name.toLowerCase().replace(".", "").replace("'", ""));
    }

    @Override
    public Player getRandomPlayer(int range) {
        int random = (int) (Math.random() * range) + 1;
        Player player = availablePlayers.get(random);
        if (player.getTeamNum() != -1) return null;
        return availablePlayers.get(random);
    }

    @Override
    public Player getNextPlayer(int n) {
        if (n == availablePlayers.size()) return null;
        return availablePlayers.get(n);
    }

    private void establishConnection() {
        //local server for now
        String url = "jdbc:mysql://localhost:3306/mockdraft?useTimezone=true&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
