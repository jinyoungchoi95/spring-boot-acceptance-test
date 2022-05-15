DROP TABLE IF EXISTS STATION;
DROP TABLE IF EXISTS LINE;
DROP TABLE IF EXISTS SECTION;

CREATE TABLE STATION
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key (id)
);

create table SECTION
(
    id              bigint auto_increment not null,
    station_id   bigint                not null,
    primary key (id),
    foreign key (station_id) references station (id)
);
