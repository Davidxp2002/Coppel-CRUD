SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[SP_Chequeos] @Justificacion VARCHAR(200), @Id_Usuario INT, @Id_Chequeo INT, @Validar int,
@Resul VARCHAR(8000) OUTPUT, @bOk INT OUTPUT, @RegistrosPagina INT, @PaginaActual INT, @HoraChequeo DATETIME
AS
BEGIN
  
  Declare @Registro INT

  SET @Registro = 0

  -- Asignar Valores
  Select @Resul  = ''
  Select @bOk  = 0
  Select @Registro = 0

  Set NoCount On
  -----------------------------------------------
  -- Verificar Paramentros de Entrada
  -----------------------------------------------

    IF @Validar = 1 -- Registrar Chequeo
    BEGIN
        IF Len(@Id_Usuario) = 0 OR LEN(@HoraChequeo) = 0
        BEGIN
            Select @Resul = 'Se necesitan todos los campos para registar el chequeo'
            Select @bOk = 0
			Return
        END
    END

    IF @Validar = 2 -- Actulizar chequeo y justificarlo
    BEGIN
        IF LEN(@Justificacion) = 0 or LEN(@Id_Chequeo) = 0
        BEGIN
            Select @Resul = 'Se necesitan todos los campos para justificar el chequeo'
            Select @bOk = 0
        END
    END

    IF @Validar = 3 -- Seleccionar los chequeos por id de usuario
    BEGIN
        IF LEN(@Id_Usuario) = 0 or LEN(@RegistrosPagina) = 0 or LEN(@PaginaActual) = 0 
        BEGIN
            Select @Resul = 'Se necesitan todos los campos para seleccionar los chequeos'
            Select @bOk = 0
        END
    END


  -----------------------------------------------
  -- Desarrollo de las Consultas
  ----------------------------------------------- 

    IF @Validar = 1 -- Registrar Chequeo
    BEGIN

        IF EXISTS (SELECT id FROM Usuarios WHERE id = @Id_Usuario)
            BEGIN
            DECLARE @DecidirEstatus VARCHAR(100)
            DECLARE @DecidirTipo VARCHAR(100)
            IF DATENAME(HOUR, @HoraChequeo) = 8 AND DATENAME(MINUTE, @HoraChequeo) >= 0
            AND DATENAME(MINUTE, @HoraChequeo) <= 15
            BEGIN
                SET @DecidirEstatus = 'Puntual'
                SET @DecidirTipo = 'Entrada por la mañana'
            END

            IF DATENAME(HOUR, @HoraChequeo) = 8 AND DATENAME(MINUTE, @HoraChequeo) > 15
            BEGIN
                SET @DecidirEstatus = 'Tarde'
                SET @DecidirTipo = 'Entrada por la mañana'
            END

            IF DATENAME(HOUR, @HoraChequeo) = 12 AND DATENAME(MINUTE, @HoraChequeo) >= 0
            AND DATENAME(MINUTE, @HoraChequeo) <= 15
            BEGIN
                SET @DecidirEstatus = 'Puntual'
                SET @DecidirTipo = 'Salida a Comer '
            END
            
            IF DATENAME(HOUR, @HoraChequeo) = 12 AND DATENAME(MINUTE, @HoraChequeo) > 15
            BEGIN
                SET @DecidirEstatus = 'Tarde'
                SET @DecidirTipo = 'Salida a Comer'
            END 

            IF DATENAME(HOUR, @HoraChequeo) = 14 AND DATENAME(MINUTE, @HoraChequeo) >= 0
            AND DATENAME(MINUTE, @HoraChequeo) <= 15
            BEGIN
                SET @DecidirEstatus = 'Puntual'
                SET @DecidirTipo = 'Entrada de Comer'
            END
            
            IF DATENAME(HOUR, @HoraChequeo) = 14 AND DATENAME(MINUTE, @HoraChequeo) > 15
            BEGIN
                SET @DecidirEstatus = 'Tarde'
                SET @DecidirTipo = 'Entrada de Comer'
            END 

            IF DATENAME(HOUR, @HoraChequeo) = 16 AND DATENAME(MINUTE, @HoraChequeo) >= 0
            AND DATENAME(MINUTE, @HoraChequeo) <= 15
            BEGIN
                SET @DecidirEstatus = 'Puntual'
                SET @DecidirTipo = 'Salida de labores'
            END
            
            IF DATENAME(HOUR, @HoraChequeo) = 16 AND DATENAME(MINUTE, @HoraChequeo) > 15
            BEGIN
                SET @DecidirEstatus = 'Tarde'
                SET @DecidirTipo = 'Salida de labores'
            END
            
            IF DATENAME(HOUR, @HoraChequeo) != 8 AND DATENAME(HOUR, @HoraChequeo) != 12
            AND DATENAME(HOUR, @HoraChequeo) != 14 AND DATENAME(HOUR, @HoraChequeo) != 16
            BEGIN
                SET @DecidirEstatus = 'N/A'
                SET @DecidirTipo = 'Fuera de horario laboral'
            END

            INSERT INTO CHEQUEOS(Tipo, Estatus, HoraChequeo, Justificacion, Id_Usuario)
            VALUES(@DecidirTipo, @DecidirEstatus, @HoraChequeo, @Justificacion, @Id_Usuario)

            SELECT @Registro = @@ROWCOUNT
            SELECT @bOk = 1
        END
        ELSE
        BEGIN
            Select @Resul = 'Usuario inexistente'
            Select @bOk = 0
			Return
        END
    END

    IF @Validar = 2 -- Actulizar chequeo y justificarlo
    BEGIN
        IF EXISTS (SELECT id FROM Chequeos WHERE id = @Id_Chequeo and Estatus = 'Tarde' or Estatus = 'N/A')
        BEGIN
            UPDATE Chequeos SET Justificacion = @Justificacion, Estatus = 'Justificado'
            WHERE Id = @Id_Chequeo
            SELECT @Registro = @@ROWCOUNT
            SELECT @bOk = 1
        END
        ELSE
        BEGIN
            Select @Resul = 'Checada inexistente o sin retardo'
            Select @bOk = 0
			Return
        END
    END

    IF @Validar = 3 -- Seleccionar los chequeos por id de usuario
    BEGIN
    	SELECT Id, Tipo, Estatus, HoraChequeo, Justificacion, registros = (SELECT COUNT(*) FROM Chequeos WHERE Id_Usuario = @Id_Usuario),
        Id_Usuario, Nombre = (SELECT (Nombre + ' ' + ApellidoPaterno + ' ' + ApellidoMaterno) FROM Usuarios WHERE id = @Id_Usuario)
		FROM Chequeos WHERE Id_Usuario = @Id_Usuario ORDER BY id DESC
		OFFSET (((@PaginaActual-1)*@RegistrosPagina)) ROWS FETCH NEXT @RegistrosPagina ROWS ONLY
        SELECT @Registro = @@ROWCOUNT
        SELECT @bOk = 1
    END




    -- Enviar Resultado
    IF @Registro = 0
    BEGIN
        IF @Validar = 1
        BEGIN
                Select @Resul = 'Error al registar el chequeo'
                SELECT @bOk = 0
        END
        
        IF @Validar = 2
        BEGIN
                Select @Resul = 'Error al justificar el chequeo'
                SELECT @bOk = 0
        END
        IF @Validar = 3
        BEGIN
                Select @Resul = 'Error al obtener los chequeos'
                SELECT @bOk = 0
        END
    END
    ELSE
    BEGIN
        IF @Validar = 1
        BEGIN
            Select @Resul = 'Registro exitoso'
            SELECT @bOk = 1
        END
        IF @Validar = 2
        BEGIN
            Select @Resul = 'Justificación exitosa'
            SELECT @bOk = 1
        END
        IF @Validar = 3
        BEGIN
            Select @Resul = 'Consulta exitosa'
            SELECT @bOk = 1
        END
    END
    

  Set NoCount Off

END
GO
