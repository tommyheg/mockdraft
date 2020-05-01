mockdraft

Tommy Hegerich

## Project
Create a Fantasy Football Mock Draft simulator. 
The program will (if it hasn't been done that day) 
retrieve the adp data from a site (currently the 
only site is fantasypros). It will then fill out 
the SQL database with the players.

The mock draft is done through a simple javafx 
gui (or commandline), with a CPU advancement 
level chosen beforehand (stupid, random, decent). 
The results of the user's team is later stored 
in a json file for future retrieval. 

Maybe include something to determine the best 
pick available. Use adp and standard deviations 
to run simulations for the user pick. This works
by running separate simulations for the next X 
players (maybe the players on the user's queue?) 
and returning the one with the best average 
adp. Simulations work recursively (this would be 
exponential so whatever is done calculation wise 
must be trivial).

## Note
The sql is only local now

## Purpose
To gain experience in java web scraping, SQL, JSON, 
and javafx. Also make a cool algorithm that we could 
use come fantasy football season.


## Libraries Used

    org.json:json:20190722
    org.jsoup:jsoup:1.13.1
    mysql:mysql-connector-java:8.0.13