use mockdraft;

drop table if exists copy;

create table copy like players;
insert into copy select * from players;

