CREATE DATABASE Checador;

USE Checador;

CREATE TABLE Usuarios(
    Id int IDENTITY(1,1) PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    ApellidoPaterno VARCHAR(100) NOT NULL,
    ApellidoMaterno VARCHAR(100) NULL
);


CREATE TABLE Chequeos(
    Id BIGINT IDENTITY(1,1) PRIMARY KEY,
    Tipo VARCHAR(100) NOT NULL,
    Estatus VARCHAR(50) NOT NULL,
    HoraChequeo DATETIME NOT NULL,
    Justificacion VARCHAR(200) NULL,
    Id_Usuario int NOT NULL
);

INSERT INTO Usuarios VALUES('David', 'Aguirre', 'Mariscal')
INSERT INTO Usuarios VALUES('Jesus', 'Coronel', 'N/A')



