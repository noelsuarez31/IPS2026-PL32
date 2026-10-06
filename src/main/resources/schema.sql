--Primero se deben borrar todas las tablas (de detalle a maestro) y lugo anyadirlas (de maestro a detalle)
--(en este caso en cada aplicacion se usa solo una tabla, por lo que no hace falta)

--Para giis.demo.tkrun:
Drop table if exists Entrada;
drop table if exists Venta;
drop table if exists Partido;
drop table if exists Butaca;
drop table if exists Equipo;

CREATE TABLE IF NOT EXISTS "butaca" (
	"id_butaca"	INTEGER,
	"tribuna"	TEXT NOT NULL CHECK("tribuna" IN ('A', 'B', 'C', 'D')),
	"seccion"	TEXT NOT NULL CHECK("seccion" IN ('A', 'B', 'C', 'D', 'E', 'F')),
	"fila"	INTEGER NOT NULL CHECK("fila" BETWEEN 1 AND 10),
	"asiento"	INTEGER NOT NULL CHECK("asiento" BETWEEN 1 AND 15),
	PRIMARY KEY("id_butaca"),
	UNIQUE("tribuna","seccion","fila","asiento")
);
CREATE TABLE IF NOT EXISTS "entrada" (
	"id_entrada"	INTEGER,
	"id_partido"	INTEGER NOT NULL,
	"id_venta"	INTEGER NOT NULL,
	"id_butaca"	INTEGER NOT NULL,
	"precio"	REAL NOT NULL DEFAULT 30.0,
	PRIMARY KEY("id_entrada"),
	UNIQUE("id_partido","id_butaca"),
	FOREIGN KEY("id_butaca") REFERENCES "butaca"("id_butaca"),
	FOREIGN KEY("id_partido") REFERENCES "partido"("id_partido"),
	FOREIGN KEY("id_venta") REFERENCES "venta"("id_venta")
);
CREATE TABLE IF NOT EXISTS "equipo" (
	"id_equipo"	INTEGER,
	"name"	TEXT NOT NULL,
	"es_propio"	INTEGER NOT NULL CHECK("es_propio" IN (0, 1)),
	PRIMARY KEY("id_equipo")
);
CREATE TABLE IF NOT EXISTS "partido" (
	"id_partido"	INTEGER,
	"fecha"	DATE NOT NULL,
	"id_local"	INTEGER NOT NULL,
	"id_visitante"	INTEGER NOT NULL,
	PRIMARY KEY("id_partido"),
	FOREIGN KEY("id_local") REFERENCES "equipo"("id_equipo"),
	FOREIGN KEY("id_visitante") REFERENCES "equipo"("id_equipo"),
	CHECK("id_local" <> "id_visitante")
);
CREATE TABLE IF NOT EXISTS "venta" (
	"id_venta"	INTEGER,
	"fecha"	TEXT NOT NULL,
	"concepto"	TEXT,
	"tipo"	TEXT NOT NULL CHECK("tipo" IN ('ENTRADAS', 'MERCHANDISING')),
	"total"	REAL NOT NULL,
	PRIMARY KEY("id_venta")
);