--Primero se deben borrar todas las tablas (de detalle a maestro) y lugo anyadirlas (de maestro a detalle)
--(en este caso en cada aplicacion se usa solo una tabla, por lo que no hace falta)

--Para giis.demo.tkrun:
Drop table if exists Entrada;
drop table if exists Venta;
drop table if exists Partido;
drop table if exists Butaca;

drop table if exists BaseEmpleado;
drop table if exists EmpleadoDeportivo;
drop table if exists EmpleadoNoDeportivo;
drop table if exists Entrenador;
drop table if exists CategoriaEquipo;
drop table if exists Equipo;

drop table if exists HorarioPeriodico;

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

CREATE TABLE IF NOT EXISTS "BaseEmpleado" (
    "dni" TEXT,
    "nombre" TEXT NOT NULL,
    "apellido" TEXT NOT NULL,
    "salario" NUMERIC NOT NULL CHECK(salario > 0),
    "fecha_nacimiento" DATE NOT NULL,
    "numero_de_telefono" TEXT NOT NULL UNIQUE,
    PRIMARY KEY("dni")
);

CREATE TABLE IF NOT EXISTS "EmpleadoDeportivo" (
	"dni" TEXT,
	"posicion" TEXT NOT NULL,
	"id_equipo"	INTEGER,
	PRIMARY KEY("dni"),
	FOREIGN KEY("dni") REFERENCES "BaseEmpleado"("dni") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("id_equipo") REFERENCES "Equipo"("id_equipo") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS "EmpleadoNoDeportivo" (
    "dni" TEXT,
    "posicion" TEXT NOT NULL,
    PRIMARY KEY("dni"),
    FOREIGN KEY("dni") REFERENCES "BaseEmpleado"("dni") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS "Entrenador" (
    "dni" TEXT,
    "tipo_entrenador" TEXT NOT NULL,
    PRIMARY KEY("dni"),
    FOREIGN KEY("dni") REFERENCES "EmpleadoDeportivo"("dni") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS "CategoriaEquipo" (
    "nombre" TEXT NOT NULL,
    "id_categoria" INTEGER PRIMARY KEY AUTOINCREMENT,
    "edad_minima" INTEGER NOT NULL,
    "edad_maxima" INTEGER NOT NULL,
    "tipo_equipo" TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "Equipo" (
    "id_equipo" INTEGER PRIMARY KEY AUTOINCREMENT,
    "nombre" TEXT NOT NULL,
    "es_propio" INTEGER NOT NULL CHECK("es_propio" IN (0, 1)),
    "tipo_equipo" TEXT NOT NULL,
    "id_categoria" INTEGER NOT NULL,
    FOREIGN KEY("id_categoria") REFERENCES "CategoriaEquipo"("id_categoria") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS "horario_periodico" (
	"id_horario"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_empleado"	INTEGER NOT NULL,
	"dia_semana"	INTEGER NOT NULL CHECK("dia_semana" BETWEEN 1 AND 7),
	"hora_inicio"	TEXT NOT NULL,
	"hora_fin"		TEXT NOT NULL
);