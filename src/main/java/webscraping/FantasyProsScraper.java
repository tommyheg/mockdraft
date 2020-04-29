package webscraping;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pojos.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FantasyProsScraper extends WebScraper{

    private final static String url="https://www.fantasypros.com/nfl/rankings/consensus-cheatsheets.php";

    /**
     * Loop through the list of players at fantasypros.com
     * @param limit- number of players you want to get (roughly)
     * @return- list of players to be added to database
     */
    public List<Player> getPlayers(int limit) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String css = "#rank-data > tbody:nth-child(3)";

        Element table = doc.select(css).get(0);
        Elements rows = table.children();

        List<Player> players = new ArrayList<Player>();
        for(int i=0;i<limit;i++){
            Element row = rows.get(i);
            String type = row.className();
            if(!type.endsWith("player-row")) continue;
            Player player = getPlayer(row);
            players.add(player);
        }

        return players;
    }

    /**
     * Create a new player with the data from the row
     * @param row- the html row that contains the player
     * @return- a new player to be added to the list
     */
    protected Player getPlayer(Element row){
        int rank = Integer.parseInt(row.child(0).ownText());    //get the rank

        Element player = row.child(2);
        String name = player.children().get(0).children().get(0).ownText(); //get name--might be better way to do this
        String team = player.children().get(1).ownText();   //get team

        String position = row.child(3).ownText();   //get position

        System.out.println(rank+": "+name+", "+team+", "+position);

        return new Player(rank, name, position, team);
    }








    public static void main(String[] args){

        FantasyProsScraper fps = new FantasyProsScraper();
        fps.getPlayers(10);

    }


}