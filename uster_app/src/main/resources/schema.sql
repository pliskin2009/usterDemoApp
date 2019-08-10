create table drivers
(
   id long not null,
   name varchar(255) not null,
   surname varchar(255) not null,
   license varchar(1) not null,
   primary key(id)
);

create table vehicles
(
   id long not null,
   model varchar(255) not null,
   brand varchar(255) not null,
   plate varchar(255) not null,
   licenserequired varchar(1) not null,
   primary key(id)
);

create table trips
(
   id long not null,
   id_vehicle long not null,
   id_driver long not null,
   trip_date date not null,
   primary key(id)
);