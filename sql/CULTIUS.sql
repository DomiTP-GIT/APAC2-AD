create database cultivos;

use cultivos;

create table TIPUS_CULTIUS (cod int primary key auto_increment, nom varchar(50));

create table PAISOS (cod int primary key auto_increment, nom varchar(50), con varchar(20));

create table PROVINCIES (cod int, pai int, nom varchar(20), hab int, primary key (cod, pai), foreign key (pai) references PAISOS(cod) on update cascade);
    
create table COMARCAS (cod varchar(20) primary key, nom varchar(20) not null, pro int, pai int, foreign key (pro, pai) references PROVINCIES(cod, pai) on update cascade);

create table CULTIVAR (cul varchar(10), com varchar(10), primary key (cul, com), foreign key (cul) references TIPUS_CULTIUS(cod) on update cascade, foreign key (com) references COMARCAS(cod) on update cascade);

drop table COMARCAS;

show tables;
insert into TIPUS_CULTIUS values ("ARR", "Arros");
insert into PAISOS(nom, con) values ("Espanya", "Europa");

describe PAISOS;

describe COMARCAS;

insert into PROVINCIES value (1,1,"Valencia",1000000), (2,1,"Alicante",700000), (3,1,"Castelló",500000), (4,1,"Madrid",2000000);

delete from PROVINCIES where cod=4;			-- Borrar datos // Deja borrar porque código es clave.

update PROVINCIES set hab = hab * 1.10;

select * from comarcas;

insert into COMARCAS(cod,nom) value ("RB", "Ribera Baixa");

update comarcas set nom = concat("La ", nom) where cod="RB";




