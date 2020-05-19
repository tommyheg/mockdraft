use mockdraft;

drop table if exists players;

create table players like copy;
insert into players select * from copy;