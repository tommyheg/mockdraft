mockdraft

by Tommy Hegerich

## Project
Create a Fantasy Football Mock Draft simulator. 
The program will (if it hasn't been done that day) 
retrieve the adp data from a site (currently the 
only site is fantasypros). It will then fill out 
the SQL database with the players, and when done, 
add teams to the SQL database with the players 
selected (the SQL will be used in the draft). 
After that, the database is either copied over 
into a json file or another SQL database 
(haven't decided yet).

The mock draft is done through a simple javafx 
gui (or commandline), with a CPU advancement 
level chosen beforehand (stupid, random, decent). 
The results of the user's team is later stored 
in a json file for future retrieval. 


## Purpose
To gain experience in java web scraping, SQL, JSON, 
and javafx.


## Libraries Used

    org.json:json:20190722
    org.jsoup:jsoup:1.13.1
