package webscraping.scrapers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pojos.Player;
import pojos.ScoreType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FantasyProsScraper extends WebScraper {

    public FantasyProsScraper(ScoreType scoreType) {
        switch (scoreType) {
            case STANDARD:
                url = "https://www.fantasypros.com/nfl/rankings/consensus-cheatsheets.php";
                break;
            case HALF:
                url = "https://www.fantasypros.com/nfl/rankings/half-point-ppr-cheatsheets.php";
                break;
            case PPR:
                url = "https://www.fantasypros.com/nfl/rankings/ppr-cheatsheets.php";
                break;
        }
    }

    //get a list of N players on fantasy pros
    @Override
    public List<Player> getPlayers(int limit) {
        //connect to page with all the player data
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert doc != null;
        String css = "#rank-data > tbody:nth-child(3)";
        Element table = doc.select(css).get(0);
        Elements rows = table.children();

        //add each player
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            Element row = rows.get(i);
            String type = row.className();
            if (!type.endsWith("player-row")) continue;  //1st row and tier rows invalid
            Player player = getPlayer(row);
            players.add(player);
        }

        return players;
    }

    //create a new player from the row element
    private Player getPlayer(Element row) {
        //get player information
        int rank = Integer.parseInt(row.child(0).ownText());
        String name = row.child(2).children().get(0).children().get(0).ownText();
        String team = row.child(2).children().get(1).ownText();
        String position = row.child(3).ownText();

        //must go to player page to get their projections
        String link = row.child(2).child(0).attr("href");

        Map<String, Double> projections;
        if (position.startsWith("K") || position.startsWith("D")) { //don't get projections for kickers or defense
            projections = new HashMap<>();
        } else {
            projections = getProjections(link);
        }

        return new Player(rank, name, position, team, projections);
    }

    //get the player's projections with his href link
    private Map<String, Double> getProjections(String link) {
        //go to the player page
        String domain = "https://www.fantasypros.com";
        String path = domain + link;
        Document doc = null;
        try {
            doc = Jsoup.connect(path).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //go to the projections page within player page
        assert doc != null;
        String css = ".pills > li:nth-child(8) > a:nth-child(1)";
        Element table = null;
        int size = 0;
        try {
            link = doc.select(css).get(0).attr("href");
            path = domain + link;
            doc = Jsoup.connect(path).get();
            css = "div.subsection:nth-child(3) > div:nth-child(2) > div:nth-child(1) > table:nth-child(1)";
            table = doc.select(css).get(0);
            size = table.child(1).child(0).childNodeSize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Scraping did not work for: " + path);
        }

        //get all of the projections of the player
        Map<String, Double> projections = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String stat = table.child(1).child(0).child(i).ownText();
            try {
                Double value = Double.parseDouble(table.child(2).child(0).child(i).ownText());
                projections.put(stat, value);
            } catch (NumberFormatException | IndexOutOfBoundsException ignored) {

            }

        }

        return projections;
    }
}
