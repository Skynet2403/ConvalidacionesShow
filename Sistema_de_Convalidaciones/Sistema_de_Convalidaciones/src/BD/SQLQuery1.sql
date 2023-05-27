use master
create LOGIN Convalidaciones
with password ='JhoniOps100'

create database SistemaConvalidaciones

use SistemaConvalidaciones

-----------------------------------------------
create table solicitudConvalidacion(
	noControl char (8) primary key,
	tecProcedencia varchar(30),
	semestre int,
	carreraCurso varchar(30),
	carreraSolicito varchar(30),
	clavePlanEstudios varchar(10),
	noTelefono char(10),
	correo varchar(30),
	noConvalidaciones int 
)
--drop table Semestres
create table Semestres(
	noSemestre int primary key
)------------
select * from Semestres

--drop table Tecnologicos
create table Tecnologicos(
	nombreTec varchar(50) primary key
)------------
select * from Tecnologicos


create table carrera(
	clave_carrera char(4)
	nombre
)

insert into Tecnologicos values ('Instituto Tecnológico de León')
insert into Tecnologicos values ('Instituto Tecnológico de La Piedad')
insert into Tecnologicos values ('Instituto Tecnológico de Mazatlán')
insert into Tecnologicos values ('Instituto Tecnológico de Oaxaca')
insert into Tecnologicos values ('Instituto Tecnológico de Toluca')


insert into Semestres values (1)
insert into Semestres values (2)
insert into Semestres values (3)
insert into Semestres values (4)
insert into Semestres values (5)
insert into Semestres values (6)
insert into Semestres values (7)
insert into Semestres values (8)
insert into Semestres values (9)
insert into Semestres values (10)
insert into Semestres values (11)
insert into Semestres values (12)


use SistemaConvalidaciones

exec sp_grantdbaccess @loginame = 'Convalidaciones'

exec sp_addrolemember 'db_datareader', 'Convalidaciones'  --Te deja leer todas las tablas de la db

SELECT @@SERVERNAME
