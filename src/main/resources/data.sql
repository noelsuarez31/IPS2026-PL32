--Datos para carga inicial de la base de datos

--Para giis.demo.tkrun:
delete from Entrada;
delete from Venta;
delete from Partido;
delete from Butaca;
delete from HorarioPeriodico;
delete from HorarioEspecifico;
delete from EmpleadoDeportivo;
delete from EmpleadoNoDeportivo;
delete from BaseEmpleado;
delete from Equipo;
delete from CategoriaEquipo;
delete from Entrenamiento;
delete from FranjaHoraria;
delete from Entrevista;
delete from Producto;
delete from LineaVenta;

delete from Reserva;
delete from Usuario;
delete from Instalacion;

delete from Abono;
delete from Abonado;
delete from PrecioSeccion;
delete from TareaMantenimientoJardinero;
delete from TareaMantenimiento;
delete from Accion;
delete from CampanaAccionista;
delete from Campana;
delete from Accionista;

INSERT INTO "Producto" ("nombre", "tipo", "precio") VALUES 
('Sudadera', 'ParteArriba', 65.00),
('Camiseta local', 'ParteArriba', 35.00),
('Pantalon', 'ParteAbajo', 35.00),
('Taza', 'Accesorios', 12.00),
('Gorra', 'Accesorios', 16.00),
('Mochila', 'Accesorios', 19.00),
('Bufanda', 'Accesorios', 15.00),
('Pantalon de chandal', 'ParteAbajo',38.00),
('Camiseta visitante', 'ParteArriba', 35.00);

with recursive tri(c) as (values ('A'),('B'),('C'),('D')), sec(c) as (values ('A'),('B'),('C'),('D'),('E'),('F')), num(x) as (select 1 union all select x + 1 from num where x < 15) insert into Butaca (tribuna, seccion, fila, asiento) select tri.c, sec.c, f.x, a.x from tri, sec, num f, num a where f.x <= 10 order by tri.c, sec.c, f.x, a.x;

INSERT INTO "entrada" VALUES (1,1,1,1,30.0);
INSERT INTO "entrada" VALUES (2,1,1,2,30.0);
INSERT INTO "entrada" VALUES (3,1,1,3,30.0);
INSERT INTO "entrada" VALUES (4,1,1,4,30.0);
INSERT INTO "entrada" VALUES (5,1,2,9,30.0);
INSERT INTO "entrada" VALUES (6,1,2,10,30.0);
INSERT INTO "entrada" VALUES (7,1,2,11,30.0);
INSERT INTO "entrada" VALUES (8,1,3,16,30.0);
INSERT INTO "entrada" VALUES (9,1,3,17,30.0);
INSERT INTO "entrada" VALUES (10,1,3,18,30.0);
INSERT INTO "entrada" VALUES (11,1,3,19,30.0);
INSERT INTO "entrada" VALUES (12,1,3,20,30.0);
INSERT INTO "entrada" VALUES (13,1,3,21,30.0);
INSERT INTO "entrada" VALUES (14,1,3,22,30.0);
INSERT INTO "entrada" VALUES (15,1,3,23,30.0);
INSERT INTO "entrada" VALUES (16,1,3,24,30.0);
INSERT INTO "entrada" VALUES (17,1,3,25,30.0);
INSERT INTO "entrada" VALUES (18,1,3,26,30.0);
INSERT INTO "entrada" VALUES (19,1,3,27,30.0);
INSERT INTO "entrada" VALUES (20,1,3,28,30.0);
INSERT INTO "entrada" VALUES (21,1,3,29,30.0);
INSERT INTO "entrada" VALUES (22,1,3,30,30.0);
INSERT INTO "entrada" VALUES (23,1,4,33,30.0);
INSERT INTO "entrada" VALUES (24,1,4,34,30.0);
INSERT INTO "entrada" VALUES (25,1,5,5,30.0);
INSERT INTO "entrada" VALUES (26,1,5,6,30.0);
INSERT INTO "entrada" VALUES (27,1,5,7,30.0);
INSERT INTO "entrada" VALUES (28,1,5,8,30.0);
INSERT INTO "entrada" VALUES (29,1,6,31,30.0);
INSERT INTO "entrada" VALUES (30,1,6,32,30.0);
INSERT INTO "entrada" VALUES (31,1,7,35,30.0);
INSERT INTO "entrada" VALUES (32,1,7,36,30.0);
INSERT INTO "entrada" VALUES (33,1,7,37,30.0);
INSERT INTO "entrada" VALUES (34,1,7,38,30.0);
INSERT INTO "entrada" VALUES (35,1,7,39,30.0);
INSERT INTO "entrada" VALUES (36,1,7,40,30.0);
INSERT INTO "entrada" VALUES (37,1,8,155,30.0);
INSERT INTO "entrada" VALUES (38,1,8,170,30.0);
INSERT INTO "entrada" VALUES (39,1,8,185,30.0);
INSERT INTO "entrada" VALUES (40,1,8,200,30.0);
INSERT INTO "entrada" VALUES (41,1,8,215,30.0);
INSERT INTO "entrada" VALUES (42,1,8,230,30.0);
INSERT INTO "entrada" VALUES (43,1,8,245,30.0);
INSERT INTO "entrada" VALUES (44,1,8,260,30.0);
INSERT INTO "entrada" VALUES (45,1,8,275,30.0);
INSERT INTO "entrada" VALUES (46,1,8,290,30.0);
INSERT INTO "entrada" VALUES (47,1,9,160,30.0);
INSERT INTO "entrada" VALUES (48,1,9,175,30.0);
INSERT INTO "entrada" VALUES (49,1,9,190,30.0);
INSERT INTO "entrada" VALUES (50,1,9,205,30.0);
INSERT INTO "entrada" VALUES (51,1,9,220,30.0);
INSERT INTO "entrada" VALUES (52,1,9,235,30.0);
INSERT INTO "entrada" VALUES (53,1,9,250,30.0);
INSERT INTO "entrada" VALUES (54,1,9,265,30.0);
INSERT INTO "entrada" VALUES (55,1,9,280,30.0);
INSERT INTO "entrada" VALUES (56,1,9,295,30.0);
INSERT INTO "entrada" VALUES (57,2,10,1,30.0);
INSERT INTO "entrada" VALUES (58,2,10,2,30.0);
INSERT INTO "entrada" VALUES (59,2,10,3,30.0);
INSERT INTO "entrada" VALUES (60,2,10,4,30.0);
INSERT INTO "entrada" VALUES (61,2,10,5,30.0);
INSERT INTO "entrada" VALUES (62,2,11,23,30.0);
INSERT INTO "entrada" VALUES (63,2,11,24,30.0);
INSERT INTO "entrada" VALUES (64,2,11,25,30.0);
INSERT INTO "entrada" VALUES (65,1,13,65,30.0);
INSERT INTO "entrada" VALUES (66,1,13,66,30.0);
INSERT INTO "entrada" VALUES (67,1,13,67,30.0);
INSERT INTO "entrada" VALUES (68,1,13,68,30.0);
INSERT INTO "entrada" VALUES (69,1,13,69,30.0);
INSERT INTO "entrada" VALUES (70,1,13,70,30.0);
INSERT INTO "entrada" VALUES (71,1,14,121,30.0);
INSERT INTO "entrada" VALUES (72,1,14,122,30.0);
INSERT INTO "entrada" VALUES (73,1,14,123,30.0);
INSERT INTO "entrada" VALUES (74,1,14,124,30.0);
INSERT INTO "entrada" VALUES (75,1,14,125,30.0);
INSERT INTO "entrada" VALUES (76,1,14,126,30.0);
INSERT INTO "entrada" VALUES (77,1,14,127,30.0);
INSERT INTO "entrada" VALUES (78,1,14,128,30.0);
INSERT INTO "entrada" VALUES (79,1,14,129,30.0);
INSERT INTO "entrada" VALUES (80,1,14,130,30.0);
INSERT INTO "entrada" VALUES (81,1,14,131,30.0);
INSERT INTO "entrada" VALUES (82,1,14,132,30.0);
INSERT INTO "entrada" VALUES (83,1,14,133,30.0);
INSERT INTO "entrada" VALUES (84,1,14,134,30.0);
INSERT INTO "entrada" VALUES (85,1,14,135,30.0);
INSERT INTO "entrada" VALUES (86,1,15,95,30.0);
INSERT INTO "entrada" VALUES (87,1,16,102,30.0);
INSERT INTO "entrada" VALUES (88,1,17,101,30.0);
INSERT INTO "entrada" VALUES (89,1,18,90,30.0);
INSERT INTO "entrada" VALUES (90,1,19,71,30.0);
INSERT INTO "entrada" VALUES (91,1,20,97,30.0);
INSERT INTO "entrada" VALUES (92,1,20,98,30.0);
INSERT INTO "entrada" VALUES (93,1,21,139,30.0);
INSERT INTO "entrada" VALUES (94,1,22,118,30.0);
INSERT INTO "entrada" VALUES (95,1,23,117,30.0);
INSERT INTO "entrada" VALUES (96,1,24,119,30.0);
INSERT INTO "entrada" VALUES (97,1,25,53,30.0);
INSERT INTO "entrada" VALUES (98,1,26,112,30.0);
INSERT INTO "entrada" VALUES (99,1,27,80,30.0);
INSERT INTO "entrada" VALUES (100,1,27,81,30.0);
INSERT INTO "entrada" VALUES (101,1,27,82,30.0);
INSERT INTO "entrada" VALUES (102,1,27,83,30.0);

INSERT INTO "venta" VALUES (1,'2026-10-05','Entradas partido 1','ENTRADAS',120.0);
INSERT INTO "venta" VALUES (2,'2026-10-05','Entradas partido 1','ENTRADAS',90.0);
INSERT INTO "venta" VALUES (3,'2026-10-06','Entradas partido 1','ENTRADAS',450.0);
INSERT INTO "venta" VALUES (4,'2026-10-06','Entradas partido 1','ENTRADAS',60.0);
INSERT INTO "venta" VALUES (5,'2026-10-07','Entradas partido 1','ENTRADAS',120.0);
INSERT INTO "venta" VALUES (6,'2026-10-07','Entradas partido 1','ENTRADAS',60.0);
INSERT INTO "venta" VALUES (7,'2026-10-08','Entradas partido 1','ENTRADAS',180.0);
INSERT INTO "venta" VALUES (8,'2026-10-08','Entradas partido 1','ENTRADAS',300.0);
INSERT INTO "venta" VALUES (9,'2026-10-08','Entradas partido 1','ENTRADAS',300.0);
INSERT INTO "venta" VALUES (10,'2026-10-20','Entradas partido 2','ENTRADAS',150.0);
INSERT INTO "venta" VALUES (11,'2026-10-21','Entradas partido 2','ENTRADAS',90.0);
INSERT INTO "venta" VALUES (12,'2026-10-22','Camiseta y bufanda','MERCHANDISING',55.0);
INSERT INTO "venta" VALUES (13,'2026-10-03','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',180.0);
INSERT INTO "venta" VALUES (14,'2026-10-03','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',450.0);
INSERT INTO "venta" VALUES (15,'2026-10-03','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (16,'2026-10-03','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (17,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (18,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (19,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (20,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',60.0);
INSERT INTO "venta" VALUES (21,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (22,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (23,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (24,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (25,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (26,'2026-10-04','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',30.0);
INSERT INTO "venta" VALUES (27,'2026-10-06','ENTRADAS PARA EL PARTIDO 1','ENTRADAS',120.0);
INSERT INTO "venta" VALUES (28,'2026-10-09 10:15','Abono de temporada','ABONOS',350.0);
INSERT INTO "venta" VALUES (29,'2026-10-12 16:40','Campaña de accionistas','CAMPANAS_ACCIONISTAS',1200.0);
INSERT INTO "venta" VALUES (30,'2026-10-23 17:45','Venta Merchandising','MERCHANDISING',97.0);
INSERT INTO "LineaVenta" ("id_venta", "id_producto", "unidades", "precio_unitario") VALUES
(12, (SELECT id_producto FROM Producto WHERE nombre='Camiseta local'), 1, 35.0),
(12, (SELECT id_producto FROM Producto WHERE nombre='Pantalon'), 1, 35.0),
(12, (SELECT id_producto FROM Producto WHERE nombre='Taza'), 1, 12.0),
(30, (SELECT id_producto FROM Producto WHERE nombre='Sudadera'), 1, 65.0),
(30, (SELECT id_producto FROM Producto WHERE nombre='Gorra'), 2, 16.0);

INSERT INTO "venta" VALUES (35,'2026-10-28','ENTRADAS PARA EL PARTIDO 4','ENTRADAS',80.0);
INSERT INTO "venta" VALUES (36,'2026-10-29','ENTRADAS PARA EL PARTIDO 4','ENTRADAS',140.0);
INSERT INTO "entrada" VALUES (103,4,35,35,40.0);
INSERT INTO "entrada" VALUES (104,4,35,36,40.0);
INSERT INTO "entrada" VALUES (105,4,36,166,35.0);
INSERT INTO "entrada" VALUES (106,4,36,167,35.0);
INSERT INTO "entrada" VALUES (107,4,36,168,35.0);
INSERT INTO "entrada" VALUES (108,4,36,169,35.0);
INSERT INTO "venta" VALUES (37,'2026-10-29','ENTRADAS PARA EL PARTIDO 5','ENTRADAS',135.0);
INSERT INTO "venta" VALUES (38,'2026-10-30','ENTRADAS PARA EL PARTIDO 5','ENTRADAS',70.0);
INSERT INTO "entrada" VALUES (109,5,37,3,45.0);
INSERT INTO "entrada" VALUES (110,5,37,4,45.0);
INSERT INTO "entrada" VALUES (111,5,37,5,45.0);
INSERT INTO "entrada" VALUES (112,5,38,307,35.0);
INSERT INTO "entrada" VALUES (113,5,38,308,35.0);
INSERT INTO "venta" VALUES (39,'2026-10-30','ENTRADAS PARA EL PARTIDO 6','ENTRADAS',80.0);
INSERT INTO "entrada" VALUES (114,6,39,22,40.0);
INSERT INTO "entrada" VALUES (115,6,39,23,40.0);

INSERT INTO "CategoriaEquipo" ("nombre", "edad_minima", "edad_maxima", "tipo_equipo") VALUES 
('Primer equipo', 19, 40, 'Profesional'),
('Filial', 19, 40, 'Profesional'),
('Regional', 19, 40, 'En formacion'),
('Juvenil', 16, 18, 'En formacion'),
('Cadete', 14, 15, 'En formacion'),
('Infantil', 12, 13, 'En formacion'),
('Alevin', 10, 11, 'En formacion'),
('Benjamin', 8, 9, 'En formacion'),
('Prebenjamin', 6, 7, 'En formacion');
INSERT INTO "Equipo" ("id_equipo", "nombre", "es_propio", "tipo_equipo", "id_categoria") VALUES 
(1, 'Real Oviedo A', 1, 'Primer equipo', 1),
(2, 'Real Oviedo B', 1, 'Filial', 2);
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('11111111A', 'Borja', 'Bastón', 3000.00, '1992-08-24', '611223344', 'borja.baston', '$argon2id$v=19$m=65536,t=3,p=4$w0WzrcPszGiSZVKMkOc/7g$VVcgYsDNypRyIaqK1fLkgWguytFoPU2cS/nT7bmVQWc');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('22222222B', 'Jimmy', 'Suárez', 2500.00, '1996-12-31', '622334455', 'jimmy.suarez', '$argon2id$v=19$m=65536,t=3,p=4$ILq9wU+QOMXuKef8dyiasg$ONTVvI/v5xMekwmO+Pd4VxMb8u1E4tiJ8fwPu6dNUMg');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('33333333C', 'Javier', 'Calleja', 6000.00, '1978-05-12', '633445566', 'javier.calleja', '$argon2id$v=19$m=65536,t=3,p=4$l4+uPq0StJiyK1kN5NwHug$mbDsMnL35IxPKK0xDeQIRvx97+Bi8DdJotCH7djWxRU');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('44444444D', 'Nacho', 'González', 2200.00, '1985-03-15', '644556677', 'nacho.gonzalez', '$argon2id$v=19$m=65536,t=3,p=4$JknyKtCtfZFqpoUSkUfHWg$x4HkT+pbjUyJU7BHFcE6kxOxR+LEg+Oh8uiCISjeCkE');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('55555555E', 'Sergio', 'Tella', 1900.00, '1990-07-20', '655667788', 'sergio.tella', '$argon2id$v=19$m=65536,t=3,p=4$JNI8T9MAfrbQxN0t6vx4FA$hLyo2G5V0n05Twlw5pssG6KChg9lCjjnwv8Akbtm2As');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena")
VALUES ('66666666F', 'Daniel', 'Tellado', 1400.00, '1990-05-05', '632437172', 'daniel.tellado', '$argon2id$v=19$m=65536,t=3,p=4$1PwrRUCjY7+AHY63eWa36A$9M7yR35Yw09EE/PJCSZvj+XbzMPP82F/A6lD67QhAds');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('77777777G', 'Lucas', 'Vázquez', 1500.00, '2008-03-10', '677889900', 'lucas.vazquez', '$argon2id$v=19$m=65536,t=3,p=4$kYnmujn2CxhVtL4uxMoW/w$qkvG3vACbVDPviO4NbC3aG+fx4k8gCbom9K5Z3kaA7g');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('88888888H', 'Mateo', 'Alonso', 1200.00, '2009-05-14', '688990011', 'mateo.alonso', '$argon2id$v=19$m=65536,t=3,p=4$nB8frWBfR5xQY6VaPy3jpg$6dY2GwSFRgzbw2b2OdnO4i08MxB6UTFNJnxdQ0zYdYA'); 
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('99999999I', 'Alejandro', 'Pérez', 1300.00, '2007-01-20', '699001122', 'alejandro.perez', '$argon2id$v=19$m=65536,t=3,p=4$CDvdEHGSD3X/fi8Kt2kD1g$4Gc9B6XuHf+EHsYOzpRq0eLqwel7/gSESHFPk9dcgEM'); 
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('12345678J', 'David', 'Villa', 2800.00, '2000-12-03', '612345678', 'david.villa', '$argon2id$v=19$m=65536,t=3,p=4$g6iYttmBU+j1SEhmEErKTw$A4YWFhjX7BBwWW9lxol0lr+qWtWFNiAfpXfOBZa8mGE');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('87654321K', 'Santi', 'Cazorla', 3200.00, '1990-06-15', '687654321', 'santi.cazorla', '$argon2id$v=19$m=65536,t=3,p=4$FrFujF6utWY9Nl95iJbmPQ$BpYnx8n9EXqI3BbU1mAvZLDBns4OS60cXuJ0mUlOkoU');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('11223344L', 'Diego', 'Mariño', 2100.00, '1990-05-09', '611223355', 'diego.marino', '$argon2id$v=19$m=65536,t=3,p=4$cJiBz0wOpRH7+cBmfQ/b4Q$EhzJKGqla77RACvY1s4RrFvloxg8926bgJ7/1PYXVlg'); 
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('55667788M', 'Manu', 'García', 2400.00, '1998-01-02', '655667799', 'manu.garcia', '$argon2id$v=19$m=65536,t=3,p=4$Wh0Z0Okd40anrDnwkxrGlg$+TKPj2Io4hOFgmc12ICzG+cJq68cI+sQVXlt8Yxmuxo');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('99887766N', 'Pelayo', 'Fernández', 1100.00, '2008-09-18', '699887766', 'pelayo.fernandez', '$argon2id$v=19$m=65536,t=3,p=4$2+mwfgMONvSm96g75GJC3Q$UqmR+yIsxmJZI8vWlPfJv20aAiKwGJk2PArZ10thdd0'); 
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('33445566O', 'Pablo', 'Álvarez', 2000.00, '1995-11-25', '633445577', 'pablo.alvarez', '$argon2id$v=19$m=65536,t=3,p=4$EwGl+9kT2rzuUvRQvXv7TQ$62xnDoZGy0yNlBLLrz7UE4T5n4n6xoZakYICzv/8410'); 
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('77889900P', 'Gaspar', 'Campos', 2600.00, '2000-03-30', '677889911', 'gaspar.campos', '$argon2id$v=19$m=65536,t=3,p=4$dNguZkISpZkOrjhn2R70tw$TWojOd58gOSlYwGMl3zD9VFD117BILe8IU74AVK9QpI'); 
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('11111111A', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('22222222B', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('33333333C', 'ENTRENADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('66666666F', 'ENTRENADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('44444444D', 'TECNICO_ADICIONAL');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('55555555E', 'TECNICO_ADICIONAL');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('77777777G', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('88888888H', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('99999999I', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('12345678J', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('87654321K', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('11223344L', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('55667788M', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('99887766N', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('33445566O', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('77889900P', 'JUGADOR');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('10101010X', 'Luis', 'García', 5000.00, '1980-04-10', '600111222', 'luis.garcia', '$argon2id$v=19$m=65536,t=3,p=4$CqP98qjMQzN125yII6t1Sg$6qabtcRpYhh99euYatGjlGgeaYplYLh9av4A6V1OknI');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('20202020Y', 'Roberto', 'Martínez', 4000.00, '1983-09-15', '600222333', 'roberto.martinez', '$argon2id$v=19$m=65536,t=3,p=4$Lsk/5nxXY4CFABG57Gqpgw$v+ZXV7iEnkHt+Kz5etuiMBVuL9bGEPtj4SQerPz6bKU');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('30303030Z', 'Alberto', 'López', 4800.00, '1979-01-22', '600333444', 'alberto.lopez', '$argon2id$v=19$m=65536,t=3,p=4$DTPz5FdZq3GKjHQKnwL/IQ$bWvj3lI8+03BAjvslmIuXNAznxBRJ5VT58sC2lh35JQ');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('40404040W', 'Carlos', 'Sánchez', 3800.00, '1986-11-05', '600444556', 'carlos.sanchez', '$argon2id$v=19$m=65536,t=3,p=4$7NDCTeLp7ac1Sc7cpCVCxg$3K992W0wh5ZE6KBNYdDLNsTxcaWb5+Ajo5U49+VL+Es');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('10101010X', 'ENTRENADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('20202020Y', 'ENTRENADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('30303030Z', 'ENTRENADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('40404040W', 'ENTRENADOR');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('50505050A', 'Mario', 'Suárez', 2000.00, '1995-02-10', '610111111', 'mario.suarez', '$argon2id$v=19$m=65536,t=3,p=4$LMcofO2fPz/FIp2ggxNWaA$JDXzIR4sMgdLwSQ5P2heoWpRm6Dlva0zl1MRsEq8n+Q');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('50505050B', 'Jorge', 'Resurrección', 2200.00, '1992-01-08', '610222222', 'jorge.resurreccion', '$argon2id$v=19$m=65536,t=3,p=4$gqLsFf4hOIajOXuv4GnNyA$f58rcMZ8kUlS6b1wpI7NuoKCbhKJR2ikd6pezwokZvM');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('50505050C', 'Rubén', 'Pardo', 1800.00, '1994-10-22', '610333333', 'ruben.pardo', '$argon2id$v=19$m=65536,t=3,p=4$Cw0Bi8TkL4ooWJaBK5RZZQ$TadYztpu/ZlkZ9iCJdmxCMCD26FmHgEyFn6hpKgz1sg');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('50505050D', 'Borja', 'Sainz', 1700.00, '2001-02-01', '610444444', 'borja.sainz', '$argon2id$v=19$m=65536,t=3,p=4$t+5GqBcOiOmmrihYGSabcA$vMzuTJrm0gENzfnRqUDahOEtEd6MYIZr+WsuXmbTbzs');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('50505050E', 'Abel', 'Bretones', 1900.00, '2000-05-21', '610555555', 'abel.bretones', '$argon2id$v=19$m=65536,t=3,p=4$0EcgBCMNOZloyQC7DIqC6Q$p/2e3reNeL8fzxr37m/koh8QpbR6M1hIoH+vjqXMjSI');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('50505050F', 'Quentin', 'Braat', 2100.00, '1997-07-06', '610666666', 'quentin.braat', '$argon2id$v=19$m=65536,t=3,p=4$EYt/26dH4u6b5fVAzx4yhg$nsOuq7sfRzq63n+qMQisuStvW+Ke8S3+khlKnlumbak');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('50505050G', 'Dani', 'Calvo', 2300.00, '1994-04-01', '610777777', 'dani.calvo', '$argon2id$v=19$m=65536,t=3,p=4$bteOOuOHEhur76DJX+O2xQ$NeJnboOOvjAsJJHgeDalxzmx4DTAHSQusSe6D55Pk/8');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('50505050A', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('50505050B', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('50505050C', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('50505050D', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('50505050E', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('50505050F', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('50505050G', 'JUGADOR');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('60606060A', 'Jon', 'Bautista', 2000.00, '1995-07-03', '620111111', 'jon.bautista', '$argon2id$v=19$m=65536,t=3,p=4$/UniJz9srnmz6RXunqSX9g$Ul9oxsc3pXLyBbm4c7oPv0Fn2nYZnlNLoKKdI4hTji8');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('60606060B', 'Víctor', 'Campuzano', 1900.00, '1997-05-31', '620222222', 'victor.campuzano', '$argon2id$v=19$m=65536,t=3,p=4$F5WvegO9qw765WbI2ydScQ$kh5XtCDeG4gBJm7y7PBn8g+VTWCgywfdVzhNY9L9E8Q');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('60606060C', 'Luka', 'Romero', 2500.00, '2004-11-18', '620333333', 'luka.romero', '$argon2id$v=19$m=65536,t=3,p=4$MstsLfPwd6ngbRt2nuMHDw$WE/tDDCDmicYefQX3nmeBjRm9wYv57nKSQ/5C+2yd1U');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('60606060D', 'Paulino', 'de la Fuente', 2100.00, '1997-06-27', '620444444', 'paulino.delafuente', '$argon2id$v=19$m=65536,t=3,p=4$Trce1VthDie01wZAHZwwcQ$KSwZZPLc1/1hrLIolixGPiweBZMCw/CtaroS9oa1sS0');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('60606060E', 'Jimmy', 'G.', 1800.00, '1999-03-12', '620555555', 'jimmy.g', '$argon2id$v=19$m=65536,t=3,p=4$dHfWg3LhF49SF0sH2OL1fw$dn+Fos8YbzoAhZrKfH0lm9p08JA12m7rLURoMdQl4e4');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('60606060F', 'Lucas', 'Ahijado', 2200.00, '1995-01-30', '620666666', 'lucas.ahijado', '$argon2id$v=19$m=65536,t=3,p=4$vqZn1eY/3sFIPq40zoS7Bw$9S/c0f0sAq+mohGv+BBRBf8nUh9TBfhJYfcCxum4JFc');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('60606060G', 'Oier', 'Luengo', 2000.00, '1996-11-11', '620777777', 'oier.luengo', '$argon2id$v=19$m=65536,t=3,p=4$y+Qxzq9l9AhJvq+sGtKwgw$4jhmbUGIR9o3lGMfbqVEYmJXlMyRNpvmRcejhe+FXH4');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('60606060A', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('60606060B', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('60606060C', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('60606060D', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('60606060E', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('60606060F', 'JUGADOR');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES ('60606060G', 'JUGADOR');

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070A', 'Carlos', 'García', 45000.00, '1980-05-12', '700111222', 'gerente', '$argon2id$v=19$m=65536,t=3,p=4$emgt/kv4dXK3uSBTYmdODQ$xeFzN6JXxA7gviT5NoX9/iPrIRqqoU/udlop6ckGCxw');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070B', 'María', 'López', 22000.00, '1995-08-23', '700222333', 'maria.lopez', '$argon2id$v=19$m=65536,t=3,p=4$AyMhXYggSUXk5g/t0C+PWg$u/UToWvfPPiqeEsCUK7D8+Yi85amKRK9ZwczfRJERZU');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070C', 'Roberto', 'Martínez', 28000.00, '1988-11-05', '700333444', 'roberto.martinez.70707070c', '$argon2id$v=19$m=65536,t=3,p=4$ntitjOH1pttHM0i+KISQgQ$fazb9Gdfw4/je+JsRDccDzfj49fdLRsN9vJKdb70jxk');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070D', 'Laura', 'Sánchez', 20000.00, '1998-02-14', '700444555', 'laura.sanchez', '$argon2id$v=19$m=65536,t=3,p=4$KqiUl9PhwHw7P/Pp33KrHw$2aLJBrQvvV7lRL2ecHDSePYmkBMGVJXsa5fxVBzyRXg');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070E', 'Javier', 'Fernández', 32000.00, '1982-07-30', '700555666', 'javier.fernandez', '$argon2id$v=19$m=65536,t=3,p=4$InyJ6rUDOyCnh86QG2D3yw$cUYRzBlUlBLvJesfdev2mV2yisM3q3A/LmKDM3QuXC4');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070F', 'Elena', 'Pérez', 19000.00, '1990-09-15', '700666777', 'elena.perez', '$argon2id$v=19$m=65536,t=3,p=4$Ooe2QMK7fa/N6IyTVkWiQQ$J3bDuhkEoIA5y0+9ernEtXse8obqIY2J3UHP8/FQNoA');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070G', 'David', 'Gómez', 21000.00, '1985-04-20', '700777888', 'david.gomez', '$argon2id$v=19$m=65536,t=3,p=4$4jY9IgBdr/49I/pEovK8YQ$j4h6l5OkO+bmz4iTvoaXNn8peyEPRvnA2Yfx7ko6epA');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070H', 'Ana', 'Ruiz', 35000.00, '1987-12-01', '700888999', 'ana.ruiz', '$argon2id$v=19$m=65536,t=3,p=4$fyxr8XsfcJNrfHVuJqC5Yg$uCXIHrfmc8WWXrKOwQp0+GI/lglGY2Y/MtZwJeAFPWg');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070I', 'Miguel', 'Torres', 22000.00, '1996-03-10', '700999000', 'miguel.torres', '$argon2id$v=19$m=65536,t=3,p=4$+SmiDCfCc8GTQ+ERmfvohg$1t1SIGnGE+VH7+h/qfk74dNy2R9d3N2QVwiSqSJjPG8');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070J', 'Sofía', 'Díaz', 20000.00, '1999-06-18', '700000111', 'sofia.diaz', '$argon2id$v=19$m=65536,t=3,p=4$HvAr0XW3iQCp04ovmgMJNw$84CRS4gh6JEOa+Hkes87iGGB78w91AA+RpWTH7UH45A');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070A', 'GERENTE');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070B', 'VENDEDOR_DE_ENTRADAS');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070C', 'ENCARGADO_DE_TIENDA');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070D', 'EMPLEADO_DE_TIENDA');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070E', 'GESTOR_DE_INSTALACIONES');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070F', 'JARDINERIA');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070G', 'COCINA');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070H', 'DIRECTOR_DE_COMUNICACIONES');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070I', 'VENDEDOR_DE_ENTRADAS');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070J', 'EMPLEADO_DE_TIENDA');

-- Nuevos jardineros para pruebas de HU 4
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070K', 'Marcos', 'Vega', 21500.00, '1992-04-10', '700000222', 'marcos.vega', '$argon2id$v=19$m=65536,t=3,p=4$38pIxoRV+QdgDqYXnHPyvg$8O9RKaT/t7giLygicHviMC57lvyK2j2Ekn/NbIcLqm0');
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") 
VALUES ('70707070L', 'Lucía', 'Molina', 21000.00, '1995-11-20', '700000333', 'lucia.molina', '$argon2id$v=19$m=65536,t=3,p=4$FLQU80UUvRItitBWy3Tx2w$gPQBjaUgPNC9NYZAXltWQp/JRfDyYhhiUYl7Nggt+u4');

INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070K', 'JARDINERIA');
INSERT INTO "EmpleadoNoDeportivo" ("dni", "posicion") VALUES ('70707070L', 'JARDINERIA');

-- Horarios periódicos para jardineros (pruebas rápidas de mantenimiento)
-- Elena Pérez (70707070F): Lunes a viernes, 08:00 a 16:00
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070F', 1, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070F', 2, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070F', 3, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070F', 4, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070F', 5, '08:00', '16:00');

-- Marcos Vega (70707070K): Lunes a viernes, 08:00 a 16:00
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070K', 1, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070K', 2, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070K', 3, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070K', 4, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070K', 5, '08:00', '16:00');

-- Lucía Molina (70707070L): Lunes a viernes, 08:00 a 16:00
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070L', 1, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070L', 2, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070L', 3, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070L', 4, '08:00', '16:00');
INSERT INTO "HorarioPeriodico" ("dni_empleado", "dia_semana", "hora_inicio", "hora_fin") VALUES ('70707070L', 5, '08:00', '16:00');

insert into Equipo (nombre, es_propio, tipo_equipo, id_categoria) values ('Real Oviedo', 1, 'Profesional', (select id_categoria from CategoriaEquipo where nombre = 'Primer equipo'));
insert into Equipo (nombre, es_propio, tipo_equipo, id_categoria) values ('Real Oviedo C', 1, 'Profesional', (select id_categoria from CategoriaEquipo where nombre = 'Primer equipo'));
insert into Equipo (nombre, es_propio, tipo_equipo, id_categoria) values ('Sporting', 0, 'Profesional', (select id_categoria from CategoriaEquipo where nombre = 'Primer equipo'));
insert into Equipo (nombre, es_propio, tipo_equipo, id_categoria) values ('Racing', 0, 'Profesional', (select id_categoria from CategoriaEquipo where nombre = 'Primer equipo'));
insert into Equipo (nombre, es_propio, tipo_equipo, id_categoria) values ('Levante', 0, 'Profesional', (select id_categoria from CategoriaEquipo where nombre = 'Primer equipo'));
insert into Equipo (nombre, es_propio, tipo_equipo, id_categoria) values ('Alavés', 0, 'Profesional', (select id_categoria from CategoriaEquipo where nombre = 'Primer equipo'));

INSERT INTO "FranjaHoraria" ("dni_jugador", "fecha", "hora_inicio", "hora_fin") VALUES 
('11111111A', '2026-10-23', '10:00:00', '11:00:00'),
('11111111A', '2026-10-24', '12:00:00', '13:00:00');

INSERT INTO "Entrevista" ("id_franja","nombre_medio") VALUES 
(1, "Mundo Deportivo"),
(2, "Marca");
INSERT INTO "Entrenamiento" ("fecha", "hora_inicio", "hora_fin", "id_equipo", "id_instalacion") VALUES 
('2026-10-21', '16:00:00', '18:00:00', 1, 1),
('2026-10-27', '17:00:00', '19:00:00', 1, 1);


insert into Instalacion (nombre) values ('Campo de fútbol 1');
insert into Instalacion (nombre) values ('Campo de fútbol 2');
insert into Instalacion (nombre) values ('Campo de fútbol 3');

insert into Usuario (nombre, tarjeta_bancaria) values ('Marta Fernández', '1111222233334444');
insert into Usuario (nombre, tarjeta_bancaria) values ('Pablo Ríos', '2222333344445555');
insert into Usuario (nombre, tarjeta_bancaria) values ('Lucía Torres', '3333444455556666');
insert into Usuario (nombre, tarjeta_bancaria) values ('Club Deportivo Naranco', '4444555566667777');

insert into Reserva (fecha, hora_inicio, hora_fin, importe, fecha_registro, id_usuario, id_instalacion) values ('2026-03-22', '14:00', '16:00', 100.0, '2026-10-21 12:41', (select id_usuario from Usuario where nombre = 'Marta Fernández'), (select id_instalacion from Instalacion where nombre = 'Campo de fútbol 1'));
insert into Reserva (fecha, hora_inicio, hora_fin, importe, fecha_registro, id_usuario, id_instalacion) values ('2026-03-22', '18:00', '19:30', 75.0, '2026-10-20 09:15', (select id_usuario from Usuario where nombre = 'Pablo Ríos'), (select id_instalacion from Instalacion where nombre = 'Campo de fútbol 1'));
insert into Reserva (fecha, hora_inicio, hora_fin, importe, fecha_registro, id_usuario, id_instalacion) values ('2026-03-31', '09:00', '11:00', 100.0, '2026-10-22 17:30', (select id_usuario from Usuario where nombre = 'Lucía Torres'), (select id_instalacion from Instalacion where nombre = 'Campo de fútbol 2'));
insert into Reserva (fecha, hora_inicio, hora_fin, importe, fecha_registro, id_usuario, id_instalacion) values ('2026-03-23', '08:00', '10:00', 100.0, '2026-10-24 13:20', (select id_usuario from Usuario where nombre = 'Pablo Ríos'), (select id_instalacion from Instalacion where nombre = 'Campo de fútbol 3'));
insert into Reserva (fecha, hora_inicio, hora_fin, importe, fecha_registro, id_usuario, id_instalacion) values ('2026-03-26', '20:00', '22:00', 100.0, '2026-10-31 10:00', (select id_usuario from Usuario where nombre = 'Club Deportivo Naranco'), (select id_instalacion from Instalacion where nombre = 'Campo de fútbol 3'));



INSERT INTO Abonado
    (dni, nombre, apellidos, fecha_nacimiento, email)
VALUES
    ('12345678A', 'Lucía', 'Fernández García', '2010-05-12',
     'lucia.fernandez@example.com'),

    ('23456789B', 'Pablo', 'Martínez López', '2005-03-20',
     'pablo.martinez@example.com'),

    ('34567890C', 'Marta', 'Suárez González', '1990-11-08',
     'marta.suarez@example.com'),

    ('45678901D', 'Antonio', 'Pérez Rodríguez', '1958-02-15',
     'antonio.perez@example.com');
     
     
INSERT INTO Venta (fecha, concepto, tipo, total)
VALUES
    ('2026-10-23', 'Abono de temporada',
     'ABONOS', 100.00),

    ('2026-10-23', 'Abono de temporada',
     'ABONOS', 200.00),

    ('2026-10-23', 'Abono de temporada',
     'ABONOS', 400.00),

    ('2026-10-23', 'Abono de temporada',
     'ABONOS', 250.00);
     
-- creamos jugadores para probar las distintas categorias --
-- Juveniles --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('81000001A', 'Hugo', 'Álvarez', 1000.00, '2008-01-15', '681000001', 'hugo.alvarez', '$argon2id$v=19$m=65536,t=3,p=4$IN6fA2iFYdIskUpQcJf6Jg$V08bYVZHwq5ljar/cL+jK0g+vnvF4t0IWXTTyFhseoQ'),
('81000002B', 'Marcos', 'Fernández', 1000.00, '2008-03-20', '681000002', 'marcos.fernandez', '$argon2id$v=19$m=65536,t=3,p=4$LuHmVpmURLJql8gj1PGZAQ$6UB2J/Roh0v/vvJmEYecy0Iy2poCnOjqurPEyI8mw0A'),
('81000003C', 'Adrián', 'Gómez', 1000.00, '2008-05-10', '681000003', 'adrian.gomez', '$argon2id$v=19$m=65536,t=3,p=4$yvfLmn4eDdluHQP2wEjioA$ba7WM5JAcBNQNfE5aRiTnY8JGQwmYzsDhaQs353Crrk'),
('81000004D', 'Iker', 'López', 1000.00, '2009-01-12', '681000004', 'iker.lopez', '$argon2id$v=19$m=65536,t=3,p=4$RdMD7rEqjEz7+/AtqPNrcA$jeDou14H5dKsjOVcisAzHF1MFbHaSMK5/+6GhY75iao'),
('81000005E', 'Rodrigo', 'Martín', 1000.00, '2009-02-18', '681000005', 'rodrigo.martin', '$argon2id$v=19$m=65536,t=3,p=4$X7XSabtZSyZnZ7n9MpVJ9g$OMd+cw9zgYQ8K9f8FAJ81IW9KtZVNdCtsNQ5+pQOYnM'),
('81000006F', 'Álvaro', 'García', 1000.00, '2009-04-25', '681000006', 'alvaro.garcia', '$argon2id$v=19$m=65536,t=3,p=4$LTuQTaT89cY7oJH4KWG/WA$OFQ6KJAgduHEMPsNq5Ju7J3yPTVp1wQw/PibnnfIJAI'),
('81000007G', 'Nicolás', 'Díaz', 1000.00, '2009-06-30', '681000007', 'nicolas.diaz', '$argon2id$v=19$m=65536,t=3,p=4$y+GYtVm5nDOHJJ4BShsPyw$gUYShUHnOpTmp0LL+BbFRc6Nf/6W1nVHWjwTs5dsq2k'),
('81000008H', 'Pablo', 'Sánchez', 1000.00, '2010-01-10', '681000008', 'pablo.sanchez', '$argon2id$v=19$m=65536,t=3,p=4$i+jOR+RGsjGtmj0mO7pKRw$/fNXqJVkUxOe6GTmMAV9h64r6KLv7aKpIm8Fg18hBBc'),
('81000009I', 'Samuel', 'Romero', 1000.00, '2010-02-14', '681000009', 'samuel.romero', '$argon2id$v=19$m=65536,t=3,p=4$BNOfytij2zwkTjDCRkTS2Q$HcGzg8YOUPmwL3njKZxfRele0l6J6vMXnxPTSPF51aA'),
('81000010J', 'Diego', 'Navarro', 1000.00, '2010-03-22', '681000010', 'diego.navarro', '$argon2id$v=19$m=65536,t=3,p=4$aHFVXjCKihcsVuepac5HUQ$iQHc/VlrX+S/NlokLlNuzaMIQssVHmMDChz5jC5+lEo');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('81000001A', 'JUGADOR'),
('81000002B', 'JUGADOR'),
('81000003C', 'JUGADOR'),
('81000004D', 'JUGADOR'),
('81000005E', 'JUGADOR'),
('81000006F', 'JUGADOR'),
('81000007G', 'JUGADOR'),
('81000008H', 'JUGADOR'),
('81000009I', 'JUGADOR'),
('81000010J', 'JUGADOR');

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('81000011K', 'Darío', 'Benítez', 1000.00, '2008-02-14', '681000011', 'dario.benitez', '$argon2id$v=19$m=65536,t=3,p=4$P+z+6PblyiBiuLPXVB2uaw$kCfpWxrXdiN31gYQdTDMj3nryU58mPtdZP1dMmQvO24'),
('81000012L', 'Joel', 'Cano', 1000.00, '2008-06-18', '681000012', 'joel.cano', '$argon2id$v=19$m=65536,t=3,p=4$CP56rAsLgB60Zucyh2Zuvw$5+TFLT/uLypGvZFUlOgztAw4TuJSnAlq+RMIRAj+7tU'),
('81000013M', 'Saúl', 'Vázquez', 1000.00, '2008-08-22', '681000013', 'saul.vazquez', '$argon2id$v=19$m=65536,t=3,p=4$I4LLTAxsqdLzGkr+aF+0kw$N0MjGkfk8rqwLG4flAoKKxfKwki6UtNNJKupLpfwY2M'),
('81000014N', 'Víctor', 'Ramos', 1000.00, '2009-01-25', '681000014', 'victor.ramos', '$argon2id$v=19$m=65536,t=3,p=4$AZ8bux9NL3bVS/tl6pQkNQ$+xyImoS14STY8eA7c433bf3cAOBFDCbORjCMO1io+AY'),
('81000015O', 'Guillermo', 'Blanco', 1000.00, '2009-05-14', '681000015', 'guillermo.blanco', '$argon2id$v=19$m=65536,t=3,p=4$+w4XLp7S/BqhPToXuc3p1w$refLrCZgmoQWAzQUv69Z5Yp2qKpP5Lt6SLc6kMvXrhw'),
('81000016P', 'Pau', 'Gil', 1000.00, '2009-09-03', '681000016', 'pau.gil', '$argon2id$v=19$m=65536,t=3,p=4$aDpX7kbu46uitYJ29lG9vg$WutrPBbDr6KI5+eSiwmlU3T81ZEf9QOJ84tWUfG6Jcs'),
('81000017Q', 'Aitor', 'Serrano', 1000.00, '2010-03-12', '681000017', 'aitor.serrano', '$argon2id$v=19$m=65536,t=3,p=4$MKykpf135lVTFckenWmVyg$pu5CFk6sAE2+1ozcPiuOPfUhYK6Zi13+z6uMfyzVJL8'),
('81000018R', 'Rubén', 'Castro', 1000.00, '2010-07-19', '681000018', 'ruben.castro', '$argon2id$v=19$m=65536,t=3,p=4$GymuJUr1gtlWOAAViJG0wA$v6i9r/dcTQZxp9l4MJQ7NlQxOUiCibCI0OO1LuvemC4');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('81000011K', 'JUGADOR'),
('81000012L', 'JUGADOR'),
('81000013M', 'JUGADOR'),
('81000014N', 'JUGADOR'),
('81000015O', 'JUGADOR'),
('81000016P', 'JUGADOR'),
('81000017Q', 'JUGADOR'),
('81000018R', 'JUGADOR');

-- Cadetes --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('82000001A', 'Mario', 'Serrano', 800.00, '2011-01-15', '682000001', 'mario.serrano', '$argon2id$v=19$m=65536,t=3,p=4$D15uoC3W3NwgRkDMiNzHjA$9vTsNkBMTzhUtn9UA4MS/SY5fbXJ2EzlBdL7tgQDG4Q'),
('82000002B', 'Daniel', 'Molina', 800.00, '2011-02-20', '682000002', 'daniel.molina', '$argon2id$v=19$m=65536,t=3,p=4$zBMbMYExYuH20nCOXFuJ1Q$CeDmGttEpRlQHkn4qw4ksTCXzASJwwjK0Qi4Y9XrpAk'),
('82000003C', 'David', 'Morales', 800.00, '2011-03-12', '682000003', 'david.morales', '$argon2id$v=19$m=65536,t=3,p=4$aM0cAig/evx5D9kAI5cB4w$co3fxP2uKE125hUHGw8vGld4eUWXSmuSyR5DALyjCzM'),
('82000004D', 'Javier', 'Ortega', 800.00, '2011-04-18', '682000004', 'javier.ortega', '$argon2id$v=19$m=65536,t=3,p=4$AQCl0Z+nZwy6Be0VxJSJ/g$nu7lQbw1XFVsmieonJhpj2J/bTFliOim1HiDTCPOPOg'),
('82000005E', 'Alejandro', 'Delgado', 800.00, '2011-05-22', '682000005', 'alejandro.delgado', '$argon2id$v=19$m=65536,t=3,p=4$zk+C/DTJeIG4bW8nFuBmCQ$3FlLdpOg1wmwa/PswEHamnNI+U29gp/3IBlid0umUF0'),
('82000006F', 'Manuel', 'Castro', 800.00, '2012-01-10', '682000006', 'manuel.castro', '$argon2id$v=19$m=65536,t=3,p=4$n/T3nIAOoXrW1W/9w7RB9w$CR/chKniHkAxHYooC+PfKIAR4wqhc2W6inPWQVRVu+8'),
('82000007G', 'Rubén', 'Ortiz', 800.00, '2012-02-14', '682000007', 'ruben.ortiz', '$argon2id$v=19$m=65536,t=3,p=4$5G+6IvvnnhiZPXPUclE2NQ$T61aFhnDswuISdS755Z/OZcZhTPTAby/6ZFP1wJWghI'),
('82000008H', 'Carlos', 'Rubio', 800.00, '2012-03-19', '682000008', 'carlos.rubio', '$argon2id$v=19$m=65536,t=3,p=4$gGxEDKY8qud1wADON+AFxA$cz9WfoushAcrXAtSQSYIQIpNnn5oroBHgrHUbdiDdgE'),
('82000009I', 'Jorge', 'Marín', 800.00, '2012-04-25', '682000009', 'jorge.marin', '$argon2id$v=19$m=65536,t=3,p=4$2ceTNLrA39yG6YoU6ZVg6A$IDRN3Pi5lR+TEQqP9I9i05VbTRxu1N3VQx08klDUcs0'),
('82000010J', 'Iván', 'Sanz', 800.00, '2012-05-30', '682000010', 'ivan.sanz', '$argon2id$v=19$m=65536,t=3,p=4$2CpYhlwKfhZze12hYtI/FA$2f4NhtbOcCYHoqUusylxhuO/mTG1SZQK8ZLcwhHiUSg');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('82000001A', 'JUGADOR'),
('82000002B', 'JUGADOR'),
('82000003C', 'JUGADOR'),
('82000004D', 'JUGADOR'),
('82000005E', 'JUGADOR'),
('82000006F', 'JUGADOR'),
('82000007G', 'JUGADOR'),
('82000008H', 'JUGADOR'),
('82000009I', 'JUGADOR'),
('82000010J', 'JUGADOR');

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('82000011K', 'Óscar', 'Mendoza', 800.00, '2011-01-20', '682000011', 'oscar.mendoza', '$argon2id$v=19$m=65536,t=3,p=4$3z1YwUN8CPtH+h7Px35HUw$KYJsVXdmcxYbJVTfTeYmRKq6KI8joJkv3LvOxT83jpg'),
('82000012L', 'Jaime', 'Guerrero', 800.00, '2011-03-15', '682000012', 'jaime.guerrero', '$argon2id$v=19$m=65536,t=3,p=4$xBOGDT3cg33gecydOsTkzg$aJs/GELqjrwUdIrDyo0qqOiw+IMOFPsnMzAdxnQHysk'),
('82000013M', 'Gabriel', 'Reyes', 800.00, '2011-06-10', '682000013', 'gabriel.reyes', '$argon2id$v=19$m=65536,t=3,p=4$L5KqdVw6zRR3OYjyflJlqA$8wgLMqVG/ylbJq8GRdCUrhvLCOeHObYz0TYgoCAAdS4'),
('82000014N', 'Marcos', 'Cortes', 800.00, '2011-08-05', '682000014', 'marcos.cortes', '$argon2id$v=19$m=65536,t=3,p=4$XwsxmwbjUO4lt4/i+dyFTQ$08Am/MnRNfnUCrBKHt4jHaWKuBypTDDzBgsNgliRLT0'),
('82000015O', 'Lucas', 'Paredes', 800.00, '2012-02-12', '682000015', 'lucas.paredes', '$argon2id$v=19$m=65536,t=3,p=4$0CY0o5jUsJqnuUBzR5XsdA$oMa61QUTKmDtjd4zvAtG0uugQUa7GSy6daSloPv/VLE'),
('82000016P', 'Adrián', 'Suárez', 800.00, '2012-04-18', '682000016', 'adrian.suarez', '$argon2id$v=19$m=65536,t=3,p=4$kteAdUYsNwDdQJl4Gcx2Vw$B90cEUbbS/DkqoDJY+SIW/CcpbqlwHRm/CDtX2Rmf1M'),
('82000017Q', 'Eric', 'Vargas', 800.00, '2012-07-22', '682000017', 'eric.vargas', '$argon2id$v=19$m=65536,t=3,p=4$gNQQNe7EYhdS/yJ34QpsJA$5RKgH/hyHe5HY9SWCfPmL7MvOym85gRYsmfzmOfxIcs'),
('82000018R', 'Samuel', 'Lara', 800.00, '2012-09-11', '682000018', 'samuel.lara', '$argon2id$v=19$m=65536,t=3,p=4$/SPQ/1n2IHngjtCddDCXVw$MUi/wNsMec3uFjE6RTb9ua7TAWhei7q3NSYud4dAWeA');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('82000011K', 'JUGADOR'),
('82000012L', 'JUGADOR'),
('82000013M', 'JUGADOR'),
('82000014N', 'JUGADOR'),
('82000015O', 'JUGADOR'),
('82000016P', 'JUGADOR'),
('82000017Q', 'JUGADOR'),
('82000018R', 'JUGADOR');

-- Infantiles --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('83000001A', 'Gonzalo', 'Iglesias', 600.00, '2013-01-15', '683000001', 'gonzalo.iglesias', '$argon2id$v=19$m=65536,t=3,p=4$G6G1IihydIeitXNIGWLnIw$ecXO79ht4t7ACAtAPQO9kkFU33uazpVxmA7KYa4gGFM'),
('83000002B', 'Héctor', 'Medina', 600.00, '2013-02-20', '683000002', 'hector.medina', '$argon2id$v=19$m=65536,t=3,p=4$38RKHfHfsUPNhb3N7vm8cQ$D63PGpq1+J04XKjcmO8q3DG2sp9pPAPGL3mcoW3ITbU'),
('83000003C', 'Guillermo', 'Cortés', 600.00, '2013-03-11', '683000003', 'guillermo.cortes', '$argon2id$v=19$m=65536,t=3,p=4$GH4pUiSYunWG78rly2ey9A$c796aPX9r7W2hvVUlARFw9ow3t250Pr3PiGEgOmnvAo'),
('83000004D', 'Jaime', 'Castillo', 600.00, '2013-04-16', '683000004', 'jaime.castillo', '$argon2id$v=19$m=65536,t=3,p=4$zXLjaalH+3Rtxm2hPwAmqA$62zl8rI4A+550/+TBaqBPRujzWwp3FY6066dYpav874'),
('83000005E', 'Raúl', 'Santos', 600.00, '2013-05-21', '683000005', 'raul.santos', '$argon2id$v=19$m=65536,t=3,p=4$oPJp0ZTdPKrXF3/U9REVnw$1kKKJG7Ipy4sc27m5Ik9bRLiAMq2tnr9UhwASzBOBFY'),
('83000006F', 'Víctor', 'Lozano', 600.00, '2014-01-12', '683000006', 'victor.lozano', '$argon2id$v=19$m=65536,t=3,p=4$IP5SH+qrzTbiTUoolcSeZQ$yVj1scME0Tkn8Pfrpo3r/Qo7onH7yusjQvjbqXgw/7g'),
('83000007G', 'Martín', 'Guerrero', 600.00, '2014-02-18', '683000007', 'martin.guerrero', '$argon2id$v=19$m=65536,t=3,p=4$p7MJ92PQ/rvkzRb6YBtSYg$WtfqGdZMqjPyCsq9B+DMntsTkz2Bc+i2bcTetg/i7ec'),
('83000008H', 'César', 'Cano', 600.00, '2014-03-24', '683000008', 'cesar.cano', '$argon2id$v=19$m=65536,t=3,p=4$V1apBlJCMsE0zM2ZMe3RQw$TdvptMLCBuy7yHpdudbcc4nVKUQSg259iu2Bo6iBPpM'),
('83000009I', 'Ángel', 'Prieto', 600.00, '2014-04-09', '683000009', 'angel.prieto', '$argon2id$v=19$m=65536,t=3,p=4$L4FPi6YoaiwRiHYOxEvszQ$ZvXJtXT+BjI4JnNBpTa2QY1jPQK/RH2xlf3Z0v2H4WY'),
('83000010J', 'Leo', 'Méndez', 600.00, '2014-05-14', '683000010', 'leo.mendez', '$argon2id$v=19$m=65536,t=3,p=4$J7hzcCLRs7jPCl7eTon2dg$DbP1o/XZZk/fnPmb+3zNNjfxq9ThGqV/qFqhILLVXAg');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('83000001A', 'JUGADOR'),
('83000002B', 'JUGADOR'),
('83000003C', 'JUGADOR'),
('83000004D', 'JUGADOR'),
('83000005E', 'JUGADOR'),
('83000006F', 'JUGADOR'),
('83000007G', 'JUGADOR'),
('83000008H', 'JUGADOR'),
('83000009I', 'JUGADOR'),
('83000010J', 'JUGADOR');

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('83000011K', 'Ismael', 'Medina', 600.00, '2013-01-22', '683000011', 'ismael.medina', '$argon2id$v=19$m=65536,t=3,p=4$BV3mlbolMxG5MMBIveXuAA$vlGZ+3KulBw4qoRSSgi9w3fzT91OuYw9aYtX0Z76/J0'),
('83000012L', 'Bruno', 'Navas', 600.00, '2013-03-30', '683000012', 'bruno.navas', '$argon2id$v=19$m=65536,t=3,p=4$+Q2AVkrjqpniUaSn+TnL9Q$WrKGO1sf9T73Mi+5rFwSMjl6PDjBnOFTH46tdFifErM'),
('83000013M', 'Fabián', 'Rojas', 600.00, '2013-05-18', '683000013', 'fabian.rojas', '$argon2id$v=19$m=65536,t=3,p=4$Tjr2IZCQCu1wBpUAMkBHCQ$vRYRhNrKN+vE8wasUYPylyAJzPoa1SfxMDwZYgYi/fc'),
('83000014N', 'Oliver', 'Santana', 600.00, '2013-08-14', '683000014', 'oliver.santana', '$argon2id$v=19$m=65536,t=3,p=4$HAgrABofi7t7upKpY57rIg$wkPMWOvzQ0pyMRj85MvgIqxE9JQnb6htsLN91A+dSl0'),
('83000015O', 'Nicolás', 'Aguilar', 600.00, '2014-02-10', '683000015', 'nicolas.aguilar', '$argon2id$v=19$m=65536,t=3,p=4$6P8IrbPwXF5H8JSVBwndaQ$PjX7meQwNeNo3Fk8cLqL7iWBvux6DpSHUgmz1CX6Su0'),
('83000016P', 'Mateo', 'Bravo', 600.00, '2014-04-20', '683000016', 'mateo.bravo', '$argon2id$v=19$m=65536,t=3,p=4$K1iwZDETKhoCu3/u82sLZA$VeA8pAmR6KQxeRcHrA5BtOhIAEXlM5XMI5cBkAA6b0w'),
('83000017Q', 'Daniel', 'Pacheco', 600.00, '2014-06-15', '683000017', 'daniel.pacheco', '$argon2id$v=19$m=65536,t=3,p=4$NVhguR7GcCceiYPhMcOykQ$0oy9Q7/7ad8M2w8o325/dAvVkirzvFtJrGH/wJ96vu8'),
('83000018R', 'Hugo', 'Morán', 600.00, '2014-09-01', '683000018', 'hugo.moran', '$argon2id$v=19$m=65536,t=3,p=4$CZDFBQnT70MqZ9CzGn7PNw$x3xdzJGFtm64VFW7uAiVWm+1aHCnh04hs4hWlabnjzc');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('83000011K', 'JUGADOR'),
('83000012L', 'JUGADOR'),
('83000013M', 'JUGADOR'),
('83000014N', 'JUGADOR'),
('83000015O', 'JUGADOR'),
('83000016P', 'JUGADOR'),
('83000017Q', 'JUGADOR'),
('83000018R', 'JUGADOR');

-- Alevines --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('84000001A', 'Enzo', 'Cruz', 500.00, '2015-01-10', '684000001', 'enzo.cruz', '$argon2id$v=19$m=65536,t=3,p=4$rx4CJWQbkkxXDPMAG+Xa8A$c7xo+YKnRPjpwklxVkQGH/jKTfd72Is20hff17YmYrU'),
('84000002B', 'Bruno', 'Calvo', 500.00, '2015-02-15', '684000002', 'bruno.calvo', '$argon2id$v=19$m=65536,t=3,p=4$QQLGwKWz1vgpUipysN97lg$BYd37bA/TGT2o6zwIplTNa3tWyJSvk0cpLBKWva8PUM'),
('84000003C', 'Oliver', 'Gallego', 500.00, '2015-03-20', '684000003', 'oliver.gallego', '$argon2id$v=19$m=65536,t=3,p=4$DZawgDf3TV3Qq9Q5RWwmoA$yWa6JF8DQGW/isuTs74rql3WEpXzkiDmvHmipuU1aMM'),
('84000004D', 'Mateo', 'Vidal', 500.00, '2015-04-25', '684000004', 'mateo.vidal', '$argon2id$v=19$m=65536,t=3,p=4$wFj6GldqhuEz+M7o75Tzpg$e4SLGy2XmE+Ptc5c9hyMQGkQ0aZ+yMhQe0GG7fdLAJs'),
('84000005E', 'Lucas', 'León', 500.00, '2015-05-30', '684000005', 'lucas.leon', '$argon2id$v=19$m=65536,t=3,p=4$rloB7zHI/oGhYzmgJLIUIg$Pdi6ycc4dJmw1fdmdacgukNMo8QANOowfFeXxRAAsOQ'),
('84000006F', 'Hugo', 'Herrera', 500.00, '2016-01-14', '684000006', 'hugo.herrera', '$argon2id$v=19$m=65536,t=3,p=4$rcB9m2/iTlINs5KHGgnm7Q$EQ+sbi57KE5ox8Dhq0USo9KX906jEyamHGdFwxfeqOA'),
('84000007G', 'Dylan', 'Márquez', 500.00, '2016-02-19', '684000007', 'dylan.marquez', '$argon2id$v=19$m=65536,t=3,p=4$dcocFbYA2ZP71TUXOhXO9Q$fytQooX3TH5JgBRJCYLLyvDQdt8w4v6k2J7ZFs201ME'),
('84000008H', 'Thiago', 'Peña', 500.00, '2016-03-23', '684000008', 'thiago.pena', '$argon2id$v=19$m=65536,t=3,p=4$xSQtnDL1UHUHH2/682E2fQ$96AeV9sklHxKnGyG7oiLDzGXnJOt/9+Zh8d8Qhtgg6c'),
('84000009I', 'Gael', 'Flores', 500.00, '2016-04-28', '684000009', 'gael.flores', '$argon2id$v=19$m=65536,t=3,p=4$XHYdz0OdlbbHPXl+sljEiw$68LrETwSM7pkQ8h8wqX808tRb68rrfmyLD8x2cAQk0k'),
('84000010J', 'Ian', 'Cabrera', 500.00, '2016-05-15', '684000010', 'ian.cabrera', '$argon2id$v=19$m=65536,t=3,p=4$g+ffBj7bos3gYEwCdyBDqw$cd55RmVLogPE198Aj5jvUyEz3NHTxL5xVOrQus0fJ2Y');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('84000001A', 'JUGADOR'),
('84000002B', 'JUGADOR'),
('84000003C', 'JUGADOR'),
('84000004D', 'JUGADOR'),
('84000005E', 'JUGADOR'),
('84000006F', 'JUGADOR'),
('84000007G', 'JUGADOR'),
('84000008H', 'JUGADOR'),
('84000009I', 'JUGADOR'),
('84000010J', 'JUGADOR');

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('84000011K', 'Alonso', 'Valero', 500.00, '2015-01-18', '684000011', 'alonso.valero', '$argon2id$v=19$m=65536,t=3,p=4$jOxkEPh4V7+H2LY5fBeH8g$e1NfwJXmHcE9+foIYvdEMrsX+mIqMYrRUlaAsn1aQ+k'),
('84000012L', 'Marcos', 'Cifuentes', 500.00, '2015-03-25', '684000012', 'marcos.cifuentes', '$argon2id$v=19$m=65536,t=3,p=4$TFGn3DMvhVHczV+gWL4LOw$ng7GDoZfVGn5lyLPogrEJ9D6jbYzIvxSbakthtom3hs'),
('84000013M', 'Biel', 'Carreño', 500.00, '2015-06-12', '684000013', 'biel.carreno', '$argon2id$v=19$m=65536,t=3,p=4$1pGyTz2V8upDYCXfM3rigw$wQwqoT1HYK7Hee4WDNZres/YH+us/GgMf2XfJsl7bvA'),
('84000014N', 'Rodrigo', 'Pallarés', 500.00, '2015-08-20', '684000014', 'rodrigo.pallares', '$argon2id$v=19$m=65536,t=3,p=4$OjL2hOzdzYuZcWZO8YnJ9g$jIVxgh6iF/4bOh763PTK+jpouL4RNXQmuv4BEo0+Hco'),
('84000015O', 'Leo', 'Salas', 500.00, '2016-02-14', '684000015', 'leo.salas', '$argon2id$v=19$m=65536,t=3,p=4$ZdNn291Rn61+QHWJ6itrTg$uC4Do7tNzjVh4HzYqahygMFWqzlrlc4G5jQIHC+zOAA'),
('84000016P', 'Iker', 'Soler', 500.00, '2016-04-10', '684000016', 'iker.soler', '$argon2id$v=19$m=65536,t=3,p=4$xIGPqeX8M6WL5R0WgHGbjA$6B866TLf6UJFvijHJBNkrb+hT+Io1B7N4DKVCmLChSg'),
('84000017Q', 'Alex', 'Moya', 500.00, '2016-07-05', '684000017', 'alex.moya', '$argon2id$v=19$m=65536,t=3,p=4$0LxPXawwLnZSHw1pvnNs1A$zmZUy443HJFUhERxKdaJERLwO+CaqILwRWpP33MuHHU'),
('84000018R', 'Luca', 'Bermejo', 500.00, '2016-09-15', '684000018', 'luca.bermejo', '$argon2id$v=19$m=65536,t=3,p=4$vIQp0Ylz3zf4/freu5Oa+w$UDHtA9jh1EgGFwM242E4vLx2/Aa9ZK7avHZWML0aq90');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('84000011K', 'JUGADOR'),
('84000012L', 'JUGADOR'),
('84000013M', 'JUGADOR'),
('84000014N', 'JUGADOR'),
('84000015O', 'JUGADOR'),
('84000016P', 'JUGADOR'),
('84000017Q', 'JUGADOR'),
('84000018R', 'JUGADOR');

-- Benjamines --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('85000001A', 'Eric', 'Campos', 400.00, '2017-01-12', '685000001', 'eric.campos', '$argon2id$v=19$m=65536,t=3,p=4$hAlV3v0dyMw1jaPJ3Nt5lA$6UjS/eGx9TZAMJHm6kMRfpIqhboSzkdGwpY5giTEqTs'),
('85000002B', 'Luca', 'Vega', 400.00, '2017-02-17', '685000002', 'luca.vega', '$argon2id$v=19$m=65536,t=3,p=4$K43D1RkF0/ZI6eHD8TiMTA$fsQBB8TYmM8PyAvlU5AyV0Uqw6rBABRd+aj2MAWQ7u0'),
('85000003C', 'Liam', 'Fuentes', 400.00, '2017-03-22', '685000003', 'liam.fuentes', '$argon2id$v=19$m=65536,t=3,p=4$clKf0VqApfJDIxkGdxTwtQ$0eQ26fMPs7eelzbrDCFed164W+bW4aoEdqDo4+DYRi4'),
('85000004D', 'Noah', 'Carrasco', 400.00, '2017-04-27', '685000004', 'noah.carrasco', '$argon2id$v=19$m=65536,t=3,p=4$fduOb+ijnBjoeOAPobmzOg$zwU3h5upV10QFTOVKXBbs8RTm2ux8VbZkpuPiBsaykA'),
('85000005E', 'Marc', 'Díez', 400.00, '2017-05-18', '685000005', 'marc.diez', '$argon2id$v=19$m=65536,t=3,p=4$aerjZLuZt1V2Ck0rTvq8MQ$KsZ6SpahjW9SQAEqrruS3se55D5Py61AYBoO8iIz52s'),
('85000006F', 'Pau', 'Caballero', 400.00, '2018-01-11', '685000006', 'pau.caballero', '$argon2id$v=19$m=65536,t=3,p=4$QrtuJlU8myI8aKB5wh43cQ$5Kkq2aNCpP3OX/VoyMOnv/hJx+1daMNQpcKnOIYM0ls'),
('85000007G', 'Biel', 'Reyes', 400.00, '2018-02-16', '685000007', 'biel.reyes', '$argon2id$v=19$m=65536,t=3,p=4$Ra3jOkJBSZwcv2MF2TU6gg$GoteKjinuQqgeso0Jyu7VpSJQIui/6hP2/hfygRMBQQ'),
('85000008H', 'Nil', 'Nieto', 400.00, '2018-03-21', '685000008', 'nil.nieto', '$argon2id$v=19$m=65536,t=3,p=4$qsekhh/H+NDWYFGIGe0kvA$N5yJV/yEUi8um6+pvyq52oLZvaoES0MJdTjk3CsgcPc'),
('85000009I', 'Pol', 'Aguilar', 400.00, '2018-04-26', '685000009', 'pol.aguilar', '$argon2id$v=19$m=65536,t=3,p=4$qMtR/W8xjbVExR5lBRvKtQ$JblvGuYZph1HQs/C+RrRquimGyOj9ep/Ph20429zgIY'),
('85000010J', 'Arnau', 'Pascual', 400.00, '2018-05-31', '685000010', 'arnau.pascual', '$argon2id$v=19$m=65536,t=3,p=4$1wU1cxK3Vb9C3JX4zNhUbg$BLfPEgNleoibVOOrE0ij2ZN32xhVOwR7jQHIWnTF+ws');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('85000001A', 'JUGADOR'),
('85000002B', 'JUGADOR'),
('85000003C', 'JUGADOR'),
('85000004D', 'JUGADOR'),
('85000005E', 'JUGADOR'),
('85000006F', 'JUGADOR'),
('85000007G', 'JUGADOR'),
('85000008H', 'JUGADOR'),
('85000009I', 'JUGADOR'),
('85000010J', 'JUGADOR');

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('85000011K', 'Jan', 'Calero', 400.00, '2017-01-20', '685000011', 'jan.calero', '$argon2id$v=19$m=65536,t=3,p=4$Rc6hZnIgJ0Hd2cIyY0pe9w$D6V5RYZm0gxbJ8HfeeB46iVvoTqNOzmj4HBpV/mitpY'),
('85000012L', 'Teo', 'Bernal', 400.00, '2017-03-15', '685000012', 'teo.bernal', '$argon2id$v=19$m=65536,t=3,p=4$RC08YKHa+dL/M1ulP1j/EQ$gQIOYipfxvuRenoq5saPGNzWVME/zp7G815++i8hCGs'),
('85000013M', 'Max', 'Gimeno', 400.00, '2017-05-22', '685000013', 'max.gimeno', '$argon2id$v=19$m=65536,t=3,p=4$3uwt1s+Oub+/jj6oBiSUaQ$95FJseu/jxBncGygcEIudP/WgQVzYBawDppsJEOm+BA'),
('85000014N', 'Martí', 'Roldán', 400.00, '2017-08-10', '685000014', 'marti.roldan', '$argon2id$v=19$m=65536,t=3,p=4$uGQKnl52YGuQkeEOFTUSJw$gh36n1Ta7mgSIbMv0WAW940vsIgWayo4QlAM/uVnQqY'),
('85000015O', 'Pol', 'Esteban', 400.00, '2018-02-18', '685000015', 'pol.esteban', '$argon2id$v=19$m=65536,t=3,p=4$s6+j8UJZWtfYMZP2kwKGGA$3WMHW2rzLENPgchf24MQ9xtzdf9Ie3tBMpCP5qu4ZbM'),
('85000016P', 'Roc', 'Chaves', 400.00, '2018-04-12', '685000016', 'roc.chaves', '$argon2id$v=19$m=65536,t=3,p=4$rYsh/B1mCUg2GaB81XqPjA$ChBAJTfqwOYx8jkOdfnBa2DKdJCU1nNwW51uS3/0dmM'),
('85000017Q', 'Nil', 'Montes', 400.00, '2018-06-25', '685000017', 'nil.montes', '$argon2id$v=19$m=65536,t=3,p=4$WkHoLMta/gHjNoS4Awr1xg$8axQ1CdUkVzIj0f1PHeGqiU9I0Yhno6sAg7aJ+qAy5E'),
('85000018R', 'Unai', 'Casas', 400.00, '2018-09-08', '685000018', 'unai.casas', '$argon2id$v=19$m=65536,t=3,p=4$RmAI0Sn8CROtEzLzmMOc8w$bLseE9QAQSqXym/mcstW3RkECPBvaExp9NS0++3ZUGk');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('85000011K', 'JUGADOR'),
('85000012L', 'JUGADOR'),
('85000013M', 'JUGADOR'),
('85000014N', 'JUGADOR'),
('85000015O', 'JUGADOR'),
('85000016P', 'JUGADOR'),
('85000017Q', 'JUGADOR'),
('85000018R', 'JUGADOR');

-- Prebenjamines --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('86000001A', 'Teo', 'Santana', 300.00, '2019-01-10', '686000001', 'teo.santana', '$argon2id$v=19$m=65536,t=3,p=4$hblgoceTm2isKLdb0aL0Ww$vbiQtyGle8cT0EoBDEAbEZNM1YFiKuNlJleO7Rj7V+I'),
('86000002B', 'Jan', 'Herrero', 300.00, '2019-02-15', '686000002', 'jan.herrero', '$argon2id$v=19$m=65536,t=3,p=4$ESzZrFHbg6cxAh/Is+L8Jw$AiT5m1jiG59J8C2mIc0zN0bb0WxPd1/tgf2FPfuWj4o'),
('86000003C', 'Alex', 'Lorenzo', 300.00, '2019-03-20', '686000003', 'alex.lorenzo', '$argon2id$v=19$m=65536,t=3,p=4$2yk/mYYh46QcXSlqAgGeGA$2cwj41A0xeeZuYhYDJkOjqC2vSsggWHa0kIi6jnj4AY'),
('86000004D', 'Jon', 'Montero', 300.00, '2019-04-25', '686000004', 'jon.montero', '$argon2id$v=19$m=65536,t=3,p=4$PdwhKW8CWarQUgg+3Dz76Q$xfukO4q737tedDXOIOHbqGGuIPV8ZppLjEBlQLbIWzg'),
('86000005E', 'Max', 'Hidalgo', 300.00, '2019-05-30', '686000005', 'max.hidalgo', '$argon2id$v=19$m=65536,t=3,p=4$VSFAId6XsZgKX4bSCG2WaA$aHD9g3+yX87TTeoXi4Y2NqgxqFNEaBZr5qN6GAxf1Rw'),
('86000006F', 'Roc', 'Giménez', 300.00, '2020-01-12', '686000006', 'roc.gimenez', '$argon2id$v=19$m=65536,t=3,p=4$4Rh0NBqwntXHmXdWbAeimQ$ZtAhL6J0YXQ9XiITI3v80hNH28dvSNFagaefe9u96J8'),
('86000007G', 'Martí', 'Ibáñez', 300.00, '2020-02-18', '686000007', 'marti.ibanez', '$argon2id$v=19$m=65536,t=3,p=4$y95n80p6VL9A1GIy4XkndQ$nA2sl4L/m10clNr79cwOZZ8jRXSoK6UGZR5QyZ5UKw8'),
('86000008H', 'Aleix', 'Ferrer', 300.00, '2020-03-24', '686000008', 'aleix.ferrer', '$argon2id$v=19$m=65536,t=3,p=4$Boj5niyUvzbSxCt2dHvsrg$k/qFuJTLTOgXkm95F9T9QHW9ACgaySIkwtxASYB1z/s'),
('86000009I', 'Brais', 'Durán', 300.00, '2020-04-19', '686000009', 'brais.duran', '$argon2id$v=19$m=65536,t=3,p=4$EVuyix9shdKLY37nywtpxA$y0e2jSHQkddZvYq2Ul623IIVj0MCTJMIdUyIKSnpZyo'),
('86000010J', 'Unai', 'Santiago', 300.00, '2020-05-15', '686000010', 'unai.santiago', '$argon2id$v=19$m=65536,t=3,p=4$Erql7YWOvK7DTGUHIZ3q4Q$BuM2EmavKyXl8C3nuQufjZXuzmGkyBOfvZss7rzkZdc');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('86000001A', 'JUGADOR'),
('86000002B', 'JUGADOR'),
('86000003C', 'JUGADOR'),
('86000004D', 'JUGADOR'),
('86000005E', 'JUGADOR'),
('86000006F', 'JUGADOR'),
('86000007G', 'JUGADOR'),
('86000008H', 'JUGADOR'),
('86000009I', 'JUGADOR'),
('86000010J', 'JUGADOR');

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('86000011K', 'Brais', 'Salguero', 300.00, '2019-01-15', '686000011', 'brais.salguero', '$argon2id$v=19$m=65536,t=3,p=4$6dMQFkmjavYPTeGr3zF00w$0usVvCQfSbVpjadL1s7IXHVoBZ20+Yqx38c5CSBUcZI'),
('86000012L', 'Aleix', 'Vergara', 300.00, '2019-03-18', '686000012', 'aleix.vergara', '$argon2id$v=19$m=65536,t=3,p=4$TVQ7HAHtsSdTBRMBl9CxuQ$BlDDYm/uKj8FN+CrM+mlGT95Z6npq+COX4+J/RMX+Zk'),
('86000013M', 'Enzo', 'Noguera', 300.00, '2019-05-20', '686000013', 'enzo.noguera', '$argon2id$v=19$m=65536,t=3,p=4$nCv4ivdJDm4l/PE6JeT1pQ$4sqYWRKwQLLsjmId88KqJIll7JCngtHJSQs+f7Nb+Qc'),
('86000014N', 'Gael', 'Pineda', 300.00, '2019-08-12', '686000014', 'gael.pineda', '$argon2id$v=19$m=65536,t=3,p=4$DyHVsVOHXcmhUruxwHda5A$PbXgppDkIRRxzaOMsBO7Og1Qenzrfy0k/lGRhPAQMDk'),
('86000015O', 'Thiago', 'Godoy', 300.00, '2020-02-14', '686000015', 'thiago.godoy', '$argon2id$v=19$m=65536,t=3,p=4$wPF3RHQCzmt6Y4oq9nKdhQ$VGmkaBBRTkWwyofhSdl2M91wp3iFdkyfczWMxnvTye4'),
('86000016P', 'Noah', 'Beltrán', 300.00, '2020-04-16', '686000016', 'noah.beltran', '$argon2id$v=19$m=65536,t=3,p=4$P9j4tlyefbdStWINhUGzbQ$AQO4K+UXQ4ip/CoWHsgYKZHQ+C1JfgWzrKvP9H20Z0I'),
('86000017Q', 'Liam', 'Alcaide', 300.00, '2020-06-22', '686000017', 'liam.alcaide', '$argon2id$v=19$m=65536,t=3,p=4$kZOSO2HdvLs2XxFVj7u+Zg$e/LesvjhlnzHw2J31SnyplwoVcDwIODSnUtGNigJsek'),
('86000018R', 'Eric', 'Castaño', 300.00, '2020-09-10', '686000018', 'eric.castano', '$argon2id$v=19$m=65536,t=3,p=4$C5jJR1KO3qB1wofGw7qnnA$MVPT9W4lNU/5nK2ZVVgyyRF6fyEhbuYuDuHUFDetUjI');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('86000011K', 'JUGADOR'),
('86000012L', 'JUGADOR'),
('86000013M', 'JUGADOR'),
('86000014N', 'JUGADOR'),
('86000015O', 'JUGADOR'),
('86000016P', 'JUGADOR'),
('86000017Q', 'JUGADOR'),
('86000018R', 'JUGADOR');

-- creamos mas tecnicos adicionales --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('91000001A', 'Raúl', 'Hernández', 2100.00, '1984-03-12', '691000001', 'raul.hernandez', '$argon2id$v=19$m=65536,t=3,p=4$ynIwjwIK7eccmik9om+Y0w$Yp4SMoJrD8QV5pjyr+vqgdrCEmwzYqEOcZLbykU6rm8'),
('91000002B', 'Marcos', 'Iglesias', 1950.00, '1988-07-25', '691000002', 'marcos.iglesias', '$argon2id$v=19$m=65536,t=3,p=4$PJznTRNi8HfVaszfYufv9A$wtxhbD0hMNUuR/7juGH2YnG7XZz9sv5QkHzjm8xWbV8'),
('91000003C', 'Adrián', 'Cano', 2200.00, '1982-11-14', '691000003', 'adrian.cano', '$argon2id$v=19$m=65536,t=3,p=4$y5u367p6Qhx2mxTCCFSDOg$LSonj9LYEr37y23iiBINBi8Iiswk52tt/QtBFSkUpaw'),
('91000004D', 'David', 'Giménez', 1900.00, '1991-05-09', '691000004', 'david.gimenez', '$argon2id$v=19$m=65536,t=3,p=4$up0pTTfeL989mOA74URVpw$AceH6VmDC+r7i+fuVbb/uMpbx1FlIc40p7yvOQNCuuc'),
('91000005E', 'Pablo', 'Vidal', 2300.00, '1986-09-30', '691000005', 'pablo.vidal', '$argon2id$v=19$m=65536,t=3,p=4$gNboTKBUugNUlX0OnULQrQ$o8pmZihYvr8hgaF3xDcLwHshh230WXWH7rhFcAsPpOY'),
('91000006F', 'Fernando', 'Romero', 2050.00, '1989-01-17', '691000006', 'fernando.romero', '$argon2id$v=19$m=65536,t=3,p=4$SHD7Hwp3WlbxCG56yjvqfw$wpwE7QTVFsAV9oot1LUdbQP0n61udC6l7Xgqx0yRAwI'),
('91000007G', 'Javier', 'Santos', 2150.00, '1985-06-22', '691000007', 'javier.santos', '$argon2id$v=19$m=65536,t=3,p=4$dDaog6KuWyPKAtzidjOdEg$fn1SIT8TZhX1HgRvMCoBU0yBGT9Nt9GtY8xAX1xJvrE'),
('91000008H', 'Álvaro', 'Morales', 2000.00, '1993-10-05', '691000008', 'alvaro.morales', '$argon2id$v=19$m=65536,t=3,p=4$VS1MG5kwz2PWcyfCR2P6bw$ZVEuuIwfX8oxnJgl8XCzL6xSm96QPLW9O2KSB9zCDD8'),
('91000009I', 'Gonzalo', 'Molina', 2250.00, '1980-04-18', '691000009', 'gonzalo.molina', '$argon2id$v=19$m=65536,t=3,p=4$hU3QwzQCJHwjQHEcqNemtg$OjBZEGE2Z+Cj4HwrXKbPtvQrmtaIavIDg77hLdShhUM'),
('91000010J', 'Santiago', 'Delgado', 1950.00, '1995-12-08', '691000010', 'santiago.delgado', '$argon2id$v=19$m=65536,t=3,p=4$FyheHQ4pVWmMh/y9ckK01Q$48XGWxen2qw2hqd572EZnn5/ISsxHixGXsqj7x8u90g');
INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('91000001A', 'TECNICO_ADICIONAL'),
('91000002B', 'TECNICO_ADICIONAL'),
('91000003C', 'TECNICO_ADICIONAL'),
('91000004D', 'TECNICO_ADICIONAL'),
('91000005E', 'TECNICO_ADICIONAL'),
('91000006F', 'TECNICO_ADICIONAL'),
('91000007G', 'TECNICO_ADICIONAL'),
('91000008H', 'TECNICO_ADICIONAL'),
('91000009I', 'TECNICO_ADICIONAL'),
('91000010J', 'TECNICO_ADICIONAL');

-- Actualizamos tipo equipo de los equipos ya creados y re asignamos a Borja Baston a uno de ellos --
UPDATE "Equipo" SET "tipo_equipo" = 'Profesional' WHERE "id_equipo" IN (1, 2);
UPDATE "EmpleadoDeportivo" SET "id_equipo" = 1 WHERE "dni" = '11111111A';

-- insertamos nuevos entrenadores para que alguno este asignado a equipos ya creados y --
-- otros para crear nuevos equipos --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('92000001A', 'Álvaro', 'Cervera', 5000.00, '1975-09-20', '692000001', 'alvaro.cervera', '$argon2id$v=19$m=65536,t=3,p=4$vJcnFizuud3Nifaguuj7Eg$elXVsvVHdQXn3vBkp1Jv6xRQW7P8lEWmiJ90IAiONGw'),
('92000002B', 'Roberto', 'Perera', 3500.00, '1980-04-12', '692000002', 'roberto.perera', '$argon2id$v=19$m=65536,t=3,p=4$qj/eI6VuZsrj2uDBdJoqWg$pzN5c+ThLbLkyEu+niFfma4n2OLtLOgv21G7n4g8kkc'),
('92000003C', 'Jaime', 'Álvarez', 3000.00, '1986-02-28', '692000003', 'jaime.alvarez', '$argon2id$v=19$m=65536,t=3,p=4$3vvHzIQALNuGU8Ejam2Uug$YgdFG5M+5wuPKbRa21B/xNivQDJkrYUA2q7BtuECtF4'),
('92000004D', 'Andrés', 'Vallina', 2500.00, '1988-10-15', '692000004', 'andres.vallina', '$argon2id$v=19$m=65536,t=3,p=4$nzoNJUqN8U2iuWpRENQARA$30N+oVJzyzUPLeepjZFEmHTRADASG4kcdBL+DXQTIBI');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion", "id_equipo") VALUES
('92000001A', 'ENTRENADOR', 1),
('92000002B', 'ENTRENADOR', 1),
('92000003C', 'ENTRENADOR', 2),
('92000004D', 'ENTRENADOR', 2);

-- lo mismo con jugadores --
INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('93000001A', 'Carlos', 'Martínez', 2800.00, '1998-04-12', '693000001', 'carlos.martinez', '$argon2id$v=19$m=65536,t=3,p=4$IA/5wRnggSOYI1fQL8bAPg$kNjVGeMow/ivEOXZsoZFyJxeuDAa9KEaxLyGqfXqTfc'),
('93000002B', 'David', 'Costas', 3000.00, '1995-03-26', '693000002', 'david.costas', '$argon2id$v=19$m=65536,t=3,p=4$d78V09RdwrZaHGJudweKfA$BR5R2qbRKoMSbB/wAgzeswVUIUnoswzIiJI/rPgR59s'),
('93000003C', 'Dani', 'Calvo', 2900.00, '1994-04-01', '693000003', 'dani.calvo.93000003c', '$argon2id$v=19$m=65536,t=3,p=4$1werWSIGjezVi27HMhAPIw$pZrzAeLetqc06yXSo4W7lc7tc4bahV8Af0acVcyzhpk'),
('93000004D', 'Rodri', 'Tarín', 2600.00, '1996-07-05', '693000004', 'rodri.tarin', '$argon2id$v=19$m=65536,t=3,p=4$wOLlsvSQhLxDHgtE0kOofw$FFwqCyUy/4tRl0GPzwcp2M0/D84UhqBae0UO5Of9EbU'),
('93000005E', 'Lucas', 'Ahijado', 2400.00, '1995-01-30', '693000005', 'lucas.ahijado.93000005e', '$argon2id$v=19$m=65536,t=3,p=4$1gP2aToJU8Ap+2mBPV+fSA$ao1nnjSXWjba2t+fRARb69iwiCMIbpXj/XNSaPxTIa4'),
('93000006F', 'Luismi', 'Sánchez', 2700.00, '1992-05-05', '693000006', 'luismi.sanchez', '$argon2id$v=19$m=65536,t=3,p=4$i+3X5aU2A2z7nY1awBiG6w$dfxDEKVdrm0PyPSDCBrIFkdL3+UXA224zFl6yjJmKhk'),
('93000007G', 'Víctor', 'Camarasa', 3100.00, '1994-05-28', '693000007', 'victor.camarasa', '$argon2id$v=19$m=65536,t=3,p=4$dRkUxdOXs6ahusGpjl83GA$eJ85vEuOfDMirFHgV0EdPctIndytlD9Y1XMWPDfW+YQ'),
('93000008H', 'Hugo', 'Rama', 2500.00, '1996-11-22', '693000008', 'hugo.rama', '$argon2id$v=19$m=65536,t=3,p=4$R4VQM3K7slVP8cwbZsdo2Q$Vdn2dX6vjVnNXL/WD1k0FtbZJykv51n3G0QPigV6R1Y'),
('93000009I', 'Viti', 'Rozada', 2600.00, '1997-09-16', '693000009', 'viti.rozada', '$argon2id$v=19$m=65536,t=3,p=4$9G6V9t4qvNBGv4weNcvuPA$KY5d1siBQkjrF89UhnUGsq8RFIFf3RBMVoJwFA3QH08'),
('93000010J', 'Masca', 'Gómez', 2300.00, '2000-07-10', '693000010', 'masca.gomez', '$argon2id$v=19$m=65536,t=3,p=4$hPh0UvpBMX7p2rlmsAwSlA$ur5aXrGP3IDGbX0iM582bIJX+Z3lvlGmEPdUGVHuHgw');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion", "id_equipo") VALUES
('93000001A', 'JUGADOR', 1),
('93000002B', 'JUGADOR', 1),
('93000003C', 'JUGADOR', 1),
('93000004D', 'JUGADOR', 1),
('93000005E', 'JUGADOR', 1),
('93000006F', 'JUGADOR', 1),
('93000007G', 'JUGADOR', 1),
('93000008H', 'JUGADOR', 1),
('93000009I', 'JUGADOR', 1),
('93000010J', 'JUGADOR', 1);

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('94000001A', 'Marco', 'Suárez', 1500.00, '2004-06-15', '694000001', 'marco.suarez', '$argon2id$v=19$m=65536,t=3,p=4$bU0UwOvVmLcum42vlH61xw$NwcEbJforwDNYgT0esQyLlPvQSZVwVbG99nAlwrLTKM'),
('94000002B', 'Charbel', 'Gómez', 1400.00, '2004-01-20', '694000002', 'charbel.gomez', '$argon2id$v=19$m=65536,t=3,p=4$zi3pJsrh34nIh6d50fPBIw$wI84AR7Pb9oHgTj+AywnKutz1ldB3OxFjzCo0AX2l/c'),
('94000003C', 'Jaime', 'Vázquez', 1400.00, '2005-02-14', '694000003', 'jaime.vazquez', '$argon2id$v=19$m=65536,t=3,p=4$iCubMEflxVV5g4/fHnzQEw$4OC+ifwz2UKl4ghsFIj4bnitvEcelmjztlcSe4NJ1qM'),
('94000004D', 'Osky', 'Menéndez', 1300.00, '2004-08-30', '694000004', 'osky.menendez', '$argon2id$v=19$m=65536,t=3,p=4$DanADICK/Mh1dNPHBK03tg$4GXVzwLoCn7jCKytG0FstrmQCB6vqjYscZFnAexOxeg'),
('94000005E', 'Yayo', 'González', 1600.00, '2004-07-18', '694000005', 'yayo.gonzalez', '$argon2id$v=19$m=65536,t=3,p=4$1dIcJ+pmhrknlekYW5elAA$rdUB5usBEnEcv71qeTM/bIXdOPBZGMHMgw96wBF+vyc'),
('94000006F', 'Víctor', 'Blanco', 1400.00, '2003-04-10', '694000006', 'victor.blanco', '$argon2id$v=19$m=65536,t=3,p=4$Yy4RIE5cNNhrQt+xFMdKOA$1QJtAvNQYqJyXTjlJtAVbgIiwIh/FtlHSRSZ9WiENW8'),
('94000007G', 'Nacho', 'Estrada', 1350.00, '2004-11-25', '694000007', 'nacho.estrada', '$argon2id$v=19$m=65536,t=3,p=4$XREQdXDyD92rRS79XNccig$1/ga4jVLclY1vodF+aUJ6oXV8ImZjPb+67J6c746qh0'),
('94000008H', 'Mario', 'Fuente', 1500.00, '2004-03-08', '694000008', 'mario.fuente', '$argon2id$v=19$m=65536,t=3,p=4$zbDn382prsS90aOPp6l/Ng$SADpdMjAsHLqG+CJWgqLDQT9N9uFz/9mDyyg+EPe7OI'),
('94000009I', 'Enric', 'Baquero', 1450.00, '2002-09-12', '694000009', 'enric.baquero', '$argon2id$v=19$m=65536,t=3,p=4$lTA+e8BAwBnRdieYIWXZBA$VAAejS7xJhT14XCW+9oj76AhJGxBGtBKev23QT8zLEI'),
('94000010J', 'Pelayo', 'Rey', 1350.00, '2004-05-19', '694000010', 'pelayo.rey', '$argon2id$v=19$m=65536,t=3,p=4$N3l/t2vqeioVHX4l6feSDQ$22btH4squsFW1vHOW45JigCVBwescomCqGg31Eypu6E');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion", "id_equipo") VALUES
('94000001A', 'JUGADOR', 2),
('94000002B', 'JUGADOR', 2),
('94000003C', 'JUGADOR', 2),
('94000004D', 'JUGADOR', 2),
('94000005E', 'JUGADOR', 2),
('94000006F', 'JUGADOR', 2),
('94000007G', 'JUGADOR', 2),
('94000008H', 'JUGADOR', 2),
('94000009I', 'JUGADOR', 2),
('94000010J', 'JUGADOR', 2);

INSERT INTO "BaseEmpleado" ("dni", "nombre", "apellido", "salario", "fecha_nacimiento", "numero_de_telefono", "nombre_usuario", "contrasena") VALUES
('95000001A', 'Adrián', 'López', 2200.00, '1998-02-10', '695000001', 'adrian.lopez', '$argon2id$v=19$m=65536,t=3,p=4$3Wcg6VDRp15IHIfbmjO26w$l4nCvF+hKzbRk2Ahpk0yRg87+rsmjbYhhjUuhl/7kRU'),
('95000002B', 'Cristian', 'Rivera', 2300.00, '1997-07-09', '695000002', 'cristian.rivera', '$argon2id$v=19$m=65536,t=3,p=4$tLczxyqZgdWF7ICuf2sFjw$KIVEKKq8lWwhYF/3f13ozTBtQIt5pFWMIFcu1ATQm60'),
('95000003C', 'Carlos', 'Dotor', 2100.00, '2001-03-15', '695000003', 'carlos.dotor', '$argon2id$v=19$m=65536,t=3,p=4$aURin1F6maAosK6vEcayuw$7EdMG2R9lGdSZob5Yai+ARMXlLRZIx3DzNgac3yC0Oo'),
('95000004D', 'Sebas', 'Moyano', 2400.00, '1997-03-23', '695000004', 'sebas.moyano', '$argon2id$v=19$m=65536,t=3,p=4$iuexT8tSRJAXs8YbTTjCLw$7VA0dAraKv1f42IN/aCtanfeLMyOC5hwjWMBbQhhYX0'),
('95000005E', 'Alemao', 'Silva', 2500.00, '1998-04-01', '695000005', 'alemao.silva', '$argon2id$v=19$m=65536,t=3,p=4$2veUUm9AR7uiiEwRcWrLmA$YjQ9ejXtbrqTIYO5IMLYf3DloIiG9GqIrN6gsuF/d28'),
('95000006F', 'Paulino', 'Guerra', 2200.00, '1997-06-27', '695000006', 'paulino.guerra', '$argon2id$v=19$m=65536,t=3,p=4$n06H71n2JJWpEH++6Eie7w$A/89/K4KoX7EdZwDEW9IUiGDa+65L9ipkarK37K7qP4'),
('95000007G', 'Álex', 'Millán', 2100.00, '1999-11-07', '695000007', 'alex.millan', '$argon2id$v=19$m=65536,t=3,p=4$u9DZ+Kpemr7snCCYHojjdQ$yCKjUY5qCZ8y7GZvJsIZPug8CqTU1xIXZErK5C5vFA8'),
('95000008H', 'Ilyas', 'Chaira', 2000.00, '2001-02-02', '695000008', 'ilyas.chaira', '$argon2id$v=19$m=65536,t=3,p=4$Oa0k+xbVN8xswiMJKvephg$vQT6f3W1TC7DbzNmf6EeEW6moSWOKUTZwcnxGIZ+Qp4'),
('95000009I', 'Rahim', 'Alhassan', 2100.00, '2002-04-26', '695000009', 'rahim.alhassan', '$argon2id$v=19$m=65536,t=3,p=4$OOjs9cpsb7pHM6oSFNeINw$TTrhhc83RgT8N71SbBDKfR7UjtOMIoRjDWygE1bbs+Q'),
('95000010J', 'Kwasi', 'Sibo', 2200.00, '1998-06-24', '695000010', 'kwasi.sibo', '$argon2id$v=19$m=65536,t=3,p=4$WS3vm6OuHt1QkMFGCcxMew$RQ+bG0yAly19AxtXfgs/oSXUpnb6pq+kGwocaxJqdXk');

INSERT INTO "EmpleadoDeportivo" ("dni", "posicion") VALUES
('95000001A', 'JUGADOR'),
('95000002B', 'JUGADOR'),
('95000003C', 'JUGADOR'),
('95000004D', 'JUGADOR'),
('95000005E', 'JUGADOR'),
('95000006F', 'JUGADOR'),
('95000007G', 'JUGADOR'),
('95000008H', 'JUGADOR'),
('95000009I', 'JUGADOR'),
('95000010J', 'JUGADOR');


INSERT INTO "Partido" ("id_partido","fecha","hora_inicio","hora_fin","id_local","id_visitante","id_instalacion","dia_del_club","precio_abonado") VALUES
(1,'2026-11-21','21:00:00','23:00:00',1,2,1,0,NULL),
(2,'2026-11-27','18:30:00','20:30:00',3,4,2,0,NULL),
(3,'2026-11-29','19:30:00','22:30:00',5,6,2,0,NULL),
(4,'2026-12-13','14:30:00','16:30:00',2,8,3,0,NULL),
(5,'2026-12-10','18:00:00','20:00:00',3,5,1,1,10.0),
(6,'2026-11-17','20:00:00','22:00:00',3,6,1,0,NULL);

INSERT INTO "PrecioSeccion" ("id_partido", "seccion", "precio")
SELECT p.id_partido, s.nombre, s.base + 
       CASE p.id_partido WHEN 2 THEN 10.0 WHEN 5 THEN 5.0 ELSE 0.0 END
FROM Partido p,
     (SELECT 'A' AS nombre, 40.0 AS base UNION ALL
      SELECT 'B', 35.0 UNION ALL
      SELECT 'C', 30.0 UNION ALL
      SELECT 'D', 25.0 UNION ALL
      SELECT 'E', 20.0 UNION ALL
      SELECT 'F', 15.0) s;
      
INSERT INTO Abono
    (id_venta, id_abonado, id_equipo, id_butaca,
     fecha_inicio, fecha_fin, fecha_registro, categoria, precio)
VALUES
    (
        (SELECT MAX(id_venta) - 3 FROM Venta),
        (SELECT id_abonado FROM Abonado WHERE dni = '12345678A'),
        (SELECT id_equipo FROM Equipo WHERE nombre = 'Real Oviedo' AND es_propio = 1 LIMIT 1),
        (SELECT id_butaca FROM Butaca
         WHERE tribuna = 'A' AND seccion = 'A' AND fila = 1 AND asiento = 1),
        '2026-07-01', '2027-06-30', '2026-10-25 12:27', 'INFANTIL', 100.00
    ),
    (
        (SELECT MAX(id_venta) - 2 FROM Venta),
        (SELECT id_abonado FROM Abonado WHERE dni = '23456789B'),
        (SELECT id_equipo FROM Equipo WHERE nombre = 'Real Oviedo' AND es_propio = 1 LIMIT 1),
        (SELECT id_butaca FROM Butaca
         WHERE tribuna = 'A' AND seccion = 'A' AND fila = 1 AND asiento = 2),
        '2026-07-01', '2027-06-30', '2026-11-27 09:15', 'SUB_26', 200.00
    ),
    (
        (SELECT MAX(id_venta) - 1 FROM Venta),
        (SELECT id_abonado FROM Abonado WHERE dni = '34567890C'),
        (SELECT id_equipo FROM Equipo WHERE nombre = 'Real Oviedo B' AND es_propio = 1 LIMIT 1),
        (SELECT id_butaca FROM Butaca
         WHERE tribuna = 'B' AND seccion = 'B' AND fila = 1 AND asiento = 1),
        '2026-07-01', '2027-06-30', '2026-10-27 11:15', 'ADULTO', 400.00
    ),
    (
        (SELECT MAX(id_venta) FROM Venta),
        (SELECT id_abonado FROM Abonado WHERE dni = '45678901D'),
        (SELECT id_equipo FROM Equipo WHERE nombre = 'Real Oviedo B' AND es_propio = 1 LIMIT 1),
        (SELECT id_butaca FROM Butaca
         WHERE tribuna = 'B' AND seccion = 'B' AND fila = 1 AND asiento = 2),
        '2026-07-01', '2027-06-30','2026-10-25 09:15', 'JUBILADO', 250.00
    );
INSERT INTO "Accionista" ("dni", "nombre", "apellidos", "email") VALUES
('71987654A', 'Lucas', 'Vega Fernández', 'lucas.vega@correo.es'),
('71876543B', 'Lucía', 'Fernández González', 'lucia.fernandez@correo.es'),
('71765432C', 'Carlos', 'Rodríguez Menéndez', 'carlos.rodriguez@correo.es'),
('71654321D', 'Elena', 'García Suárez', 'elena.garcia@correo.es');

INSERT INTO "Campana" ("id_campana", "nombre", "fase", "precio_emision", "num_acciones", "acciones_vendidas", "estado", "fecha_lanzamiento") VALUES
(1, 'Campaña 2024', 1, 10.00, 7, 7, 'CERRADA', '2024-01-01 09:00'),
(2, 'Campaña 2024', 2, 12.00, 6, 6, 'CERRADA', '2024-05-15 09:00'),
(3, 'Ampliación 2025', 1, 15.00, 5, 5, 'CERRADA', '2025-03-01 09:00');

-- Acciones de prueba (para Lucas Vega Fernández - 71987654A, exactamente como en el prototipo)
INSERT INTO "Accion" ("id_accion", "dni_propietario", "fecha_adquisicion", "precio_adquisicion", "en_venta", "precio_venta", "origen_tipo", "id_campana", "vendedor_previo") VALUES
(1, '71987654A', '2024-01-10', 10.00, 1, 18.00, 'CAMPANA', 1, NULL),
(2, '71987654A', '2024-01-11', 15.00, 1, NULL, 'COMPRA_ACCIONISTA', NULL, 'Lucía Fernández González'),
(3, '71987654A', '2025-02-03', 12.00, 0, NULL, 'COMPRA_ACCIONISTA', NULL, 'Carlos Rodríguez Menéndez'),
(4, '71987654A', '2025-03-15', 15.00, 0, NULL, 'CAMPANA', 3, NULL);

-- Acciones de prueba para otros accionistas (para un total de 20 en el club)
INSERT INTO "Accion" ("id_accion", "dni_propietario", "fecha_adquisicion", "precio_adquisicion", "en_venta", "precio_venta", "origen_tipo", "id_campana", "vendedor_previo") VALUES
(5, '71876543B', '2024-01-10', 10.00, 0, NULL, 'CAMPANA', 1, NULL),
(6, '71876543B', '2024-01-10', 10.00, 0, NULL, 'CAMPANA', 1, NULL),
(7, '71876543B', '2024-06-01', 12.00, 1, 16.00, 'CAMPANA', 2, NULL),
(8, '71876543B', '2024-06-01', 12.00, 0, NULL, 'CAMPANA', 2, NULL),
(9, '71876543B', '2025-03-15', 15.00, 0, NULL, 'CAMPANA', 3, NULL),
(10, '71765432C', '2024-01-10', 10.00, 1, 14.00, 'CAMPANA', 1, NULL),
(11, '71765432C', '2024-01-10', 10.00, 0, NULL, 'CAMPANA', 1, NULL),
(12, '71765432C', '2024-06-01', 12.00, 0, NULL, 'CAMPANA', 2, NULL),
(13, '71765432C', '2024-06-01', 12.00, 1, 15.00, 'CAMPANA', 2, NULL),
(14, '71765432C', '2025-03-15', 15.00, 0, NULL, 'CAMPANA', 3, NULL),
(15, '71654321D', '2024-01-10', 10.00, 0, NULL, 'CAMPANA', 1, NULL),
(16, '71654321D', '2024-01-10', 10.00, 0, NULL, 'CAMPANA', 1, NULL),
(17, '71654321D', '2024-06-01', 12.00, 0, NULL, 'CAMPANA', 2, NULL),
(18, '71654321D', '2024-06-01', 12.00, 0, NULL, 'CAMPANA', 2, NULL),
(19, '71654321D', '2025-03-15', 15.00, 0, NULL, 'CAMPANA', 3, NULL),
(20, '71654321D', '2025-03-15', 15.00, 0, NULL, 'CAMPANA', 3, NULL);


-- 1. Reserva externa de socio (Marta Fernández: 10:00 a 11:30, Importe: 50.0) [Sobreescribible]
INSERT INTO "Reserva" ("fecha", "hora_inicio", "hora_fin", "importe", "fecha_registro", "id_usuario", "id_instalacion") 
VALUES ('2026-11-20', '10:00', '11:30', 50.0, '2026-10-27 10:00', 1, 1);
-- 2. Franja con entrevista asignada a un medio (Carlos Martínez - Marca: 12:00 a 13:00) [Sobreescribible]
INSERT INTO "FranjaHoraria" ("dni_jugador", "fecha", "hora_inicio", "hora_fin") 
VALUES ('93000001A', '2026-11-20', '12:00', '13:00');
INSERT INTO "Entrevista" ("id_franja", "nombre_medio") 
VALUES ((SELECT last_insert_rowid()), 'Marca');
-- 3. Franja libre de otro jugador del equipo (David Costas: 13:00 a 14:00) [Sobreescribible]
INSERT INTO "FranjaHoraria" ("dni_jugador", "fecha", "hora_inicio", "hora_fin") 
VALUES ('93000002B', '2026-11-20', '13:00', '14:00');
-- 4. Entrenamiento de otro equipo en la misma instalación (Real Oviedo B: 16:00 a 17:30) [No sobreescribible - Bloqueante]
INSERT INTO "Entrenamiento" ("fecha", "hora_inicio", "hora_fin", "id_equipo", "id_instalacion") 
VALUES ('2026-11-20', '16:00', '17:30', 2, 1);
-- 5. Partido oficial en la misma instalación (Real Oviedo A vs Real Oviedo B: 18:00 a 20:00) [No sobreescribible - Bloqueante]
INSERT INTO "Partido" ("fecha", "hora_inicio", "hora_fin", "id_local", "id_visitante", "id_instalacion") 
VALUES ('2026-11-20', '19:30', '21:30', 3, 5, 1);
-- 6. Tarea de jardinería en la instalación (Corte de césped: 08:00 a 09:30) [Sobreescribible]
INSERT INTO "TareaMantenimiento" ("id_instalacion", "fecha", "hora_inicio", "hora_fin", "descripcion") 
VALUES (1, '2026-11-20', '08:00', '09:30', 'Corte de césped');
INSERT INTO "TareaMantenimientoJardinero" ("id_tarea", "dni_jardinero") 
VALUES ((SELECT last_insert_rowid()), '70707070F');

-- 7. Reserva externa de particular -> SOBREESCRIBIBLE
INSERT INTO "Reserva" ("fecha", "hora_inicio", "hora_fin", "importe", "fecha_registro", "id_usuario", "id_instalacion") 
VALUES ('2026-11-20', '11:30', '13:00', 75.0, '2026-11-01 10:00', 1, 1);
-- 8. Entrevista asignada a franja (Borja Bastón con Marca) -> SOBREESCRIBIBLE
INSERT INTO "FranjaHoraria" ("dni_jugador", "fecha", "hora_inicio", "hora_fin") 
VALUES ('11111111A', '2026-11-20', '14:00', '15:30');
INSERT INTO "Entrevista" ("id_franja", "nombre_medio") 
VALUES ((SELECT MAX(id_franja) FROM FranjaHoraria WHERE fecha = '2026-11-20' AND hora_inicio = '14:00'), 'Marca');

-- Asignar entrenador y jugadores a Real Oviedo (id_equipo = 3)
UPDATE "EmpleadoDeportivo" SET "id_equipo" = 3 WHERE "dni" = '33333333C';
UPDATE "EmpleadoDeportivo" SET "id_equipo" = 3 WHERE "dni" IN (
    '11111111A', '93000001A', '93000002B', 
    '22222222B', '77777777G', '88888888H', '99999999I', '12345678J', '87654321K', '11223344L', '55667788M', '99887766N'
);
-- Asignar entrenador y jugadores a Real Oviedo B (id_equipo = 4)
UPDATE "EmpleadoDeportivo" SET "id_equipo" = 4 WHERE "dni" = '66666666F';
UPDATE "EmpleadoDeportivo" SET "id_equipo" = 4 WHERE "dni" IN (
    '33445566O', '77889900P', '50505050A', '50505050B', '50505050C', '50505050D', '50505050E', '50505050F'
);