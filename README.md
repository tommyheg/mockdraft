mockdraft

Tommy Hegerich

## Project
Create a Fantasy Football Mock Draft simulator. 
The program will (if it hasn't been done that day) 
retrieve the adp data from a site (currently the 
only site is fantasypros). It will then fill out 
the SQL database with the players.

The mock draft is done through commandline (maybe 
javafx) with a CPU advancement level chosen beforehand 
(stupid, random, decent). The results of the user's 
team may be later stored in a json file for 
future retrieval. 

Maybe include something to determine the best 
pick available. Use adp and standard deviations 
to run simulations for the user pick. This works
by running separate simulations for the next X 
players (maybe the players on the user's queue?) 
and returning the one with the best average 
adp. Simulations work recursively (this would be 
exponential so whatever is done calculation wise 
must be trivial).

## SQL
The sql is only local now. Must setup a local sql 
database (local instance 3306. I set up the user to 
be root and password as root but you can change 
that for your local system if you want).

The sql is pretty slow and useless. Might as well just 
store everything in data structures or json as of now 
(with just the commandline option). I remember sql 
fitting nicely with javafx (so it would be really 
easy to have a table of available players in the gui). 
But if gui isn't a feature then just add the json option 
that will primarily be used.

## Purpose
To gain experience in java web scraping, SQL, JSON, 
and javafx. Also make a cool algorithm that we could 
use come fantasy football season. Maybe this could 
actually be used for quick mock drafts against the 
cpu.


## Libraries Used

    org.json:json:20190722
    org.jsoup:jsoup:1.13.1
    mysql:mysql-connector-java:8.0.16
    org.springframework:spring-test:4.2.4.RELEASE
    org.springframework:spring-jdbc:5.2.6.RELEASE
    
    
## Outlook

Data    

    -get ADP/STDV data from FFCalculator
        -place in draft data table and JSON file
    -get Projection data from FantasyPros
        -place in player table
    -merge the two tables together
        -this is for the gui
    -ADP/STDV JSON file -> 2D Array of Player-Pick-Probs
        -in Suggestor Class
    
    -do all of these 3 times (one for each scoring type)
        -also make copies of gui table for drafting
        
Draft

    -get score type, league size, user pick
    -start draft
    -Controller gets players from DataGetter
    -Controller deletes players with DataStorer
    -DataGetter and DataStorer determine which table to use by the score type
        -constructor changes the url of the connection
    -each user pick gets suggestions
        -CLUI -> Controller -> Suggestor -> Simulator
        -Suggestor uses DataGetter to get all the players
        -Suggestor calls a bunch of threads to get vals for each player
            -this is done in the Simulator class (concurrency)

Sims
    
    -each sim is a thread, run concurrently
        -in the Simulator class, simulate() method
    -run a bfs for 5-6 levels (basically rounds)
    -each player is a node
        -outgoing edges to the next N players (or something like that, 
         we could figure out which players to go to based on stdev or 
         something)
            -maybe create an actual graph before draft if it is too 
             complicated
    -each node has its own value
        -recursively go thru a bfs, using each value to get the original 
         node's value (by adding, multiplying, not sure yet)
        -once value is found, store it
            -stored in Hashmap, passed into function
                -or Hashmap is owned by the Simulator class, which is 
                 passed by reference through its constructor, so all the 
                 threads share it
            -because of concurrency, need locks for this
         






