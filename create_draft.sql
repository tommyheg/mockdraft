use mockdraft;

drop table if exists players;

set autocommit = 1;

create table players(
	ID int(3) PRIMARY KEY NOT NULL,
    LastName VarChar(255) NOT NULL,
    FirstName VarChar(255) NOT NULL,
    Position VarChar(5) NOT NULL,
    Team VarChar(5) NOT NULL,
    
	Points decimal(4, 1),
    RushAtt decimal(4, 1),
    RushYds decimal(5, 1),
    RushTds decimal(3, 1),
    Recs decimal(4, 1),
    RecYds decimal(5, 1),
    RecTds decimal(3, 1),
    PassComp decimal(4, 1),
    PassAtt decimal(4, 1),
    PassYds decimal(5, 1),
    PassTds decimal(3, 1),
    PassInts decimal(3, 1),
    Fumbles decimal(2, 1),
    FullName VarChar(255) NOT NULL
);

