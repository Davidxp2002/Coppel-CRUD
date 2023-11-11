package com.davidagm.coppelchecker.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.davidagm.coppelchecker.model.Chequeo;
import com.davidagm.coppelchecker.model.DBResponse;
import com.davidagm.coppelchecker.model.DBResponseWithChecadas;
import com.davidagm.coppelchecker.model.NewChequeo;
import com.davidagm.coppelchecker.model.NewJustificacion;
import com.davidagm.coppelchecker.model.PaginatorChequeos;

import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ChequeosRepository implements IChequeosRepository{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	@Override
	public DBResponse save(NewChequeo chequeo) {
		
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_Chequeos")
                .declareParameters(
                		new SqlParameter("@Justificacion", Types.VARCHAR),
                		new SqlParameter("@Id_Usuario", Types.INTEGER),
                		new SqlParameter("@Id_Chequeo", Types.INTEGER),
                        new SqlParameter("@Validar", Types.INTEGER),
                        new SqlParameter("@HoraChequeo", Types.TIMESTAMP)
                );

		System.out.print("HORAAAAA =>>>>" + LocalDateTime.now() + "FINNNN");
		
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("@Justificacion", "");
        inParams.put("@Id_Usuario", chequeo.idUsuario);
        inParams.put("@Id_Chequeo", 0);
        inParams.put("@Validar", 1);
        inParams.put("@Resul", "");
        inParams.put("@bOk", 0);
        inParams.put("@RegistrosPagina", 0);
        inParams.put("@PaginaActual", 0);
        inParams.put("@HoraChequeo", LocalDateTime.now());

        Map<String, Object> result = jdbcCall.execute(inParams);

        DBResponse dbResponse = new DBResponse();
        dbResponse.Resul = ((String)result.get("Resul"));
        dbResponse.bOk = ((Integer)result.get("bOk"))  == 1 ? true : false;

        return dbResponse;
	}


	@Override
	public DBResponseWithChecadas findChequeosById(PaginatorChequeos paginatorChequeos) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_Chequeos")
                .declareParameters(
                		new SqlParameter("@Justificacion", Types.VARCHAR),
                		new SqlParameter("@Id_Usuario", Types.INTEGER),
                		new SqlParameter("@Id_Chequeo", Types.INTEGER),
                        new SqlParameter("@Validar", Types.INTEGER),
                        new SqlParameter("@HoraChequeo", Types.TIMESTAMP)
                        
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("@Justificacion", "");
        inParams.put("@Id_Usuario", paginatorChequeos.idUsuario);
        inParams.put("@Id_Chequeo", 0);
        inParams.put("@Validar", 3);
        inParams.put("@Resul", "");
        inParams.put("@bOk", 0);
        inParams.put("@RegistrosPagina", paginatorChequeos.registrosPagina);
        inParams.put("@PaginaActual", paginatorChequeos.paginaActual);
        inParams.put("@HoraChequeo", LocalDateTime.now());
        

        Map<String, Object> result = jdbcCall.execute(inParams);
         System.out.print(result);
        DBResponseWithChecadas dbResponse = new DBResponseWithChecadas();
        dbResponse.Resul = ((String)result.get("Resul"));
        dbResponse.bOk = ((Integer)result.get("bOk"))  == 1 ? true : false;
        
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        List<Chequeo> checadas = new ArrayList<>();
        if (resultSet != null) {
            for (Map<String, Object> row : resultSet) {
                Chequeo chequeo = new Chequeo();
                chequeo.id = ((Long)row.get("Id"));
                chequeo.tipo = ((String)row.get("Tipo"));
                chequeo.estatus = ((String)row.get("Estatus"));
                chequeo.horachequeo = ((Date)row.get("HoraChequeo"));
                chequeo.justificacion = ((String)row.get("Justificacion"));
                chequeo.idUsuario = ((Integer)row.get("Id_Usuario"));
                chequeo.registros = ((Integer)row.get("registros"));
                chequeo.nombreUsuario = ((String)row.get("Nombre"));
                checadas.add(chequeo);
            }
        }
        
        dbResponse.checadas = checadas;
        
        return dbResponse;
	}


	@Override
	public DBResponse justificar(NewJustificacion newJustificacion) {
		
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_Chequeos")
                .declareParameters(
                		new SqlParameter("@Justificacion", Types.VARCHAR),
                		new SqlParameter("@Id_Usuario", Types.INTEGER),
                		new SqlParameter("@Id_Chequeo", Types.INTEGER),
                        new SqlParameter("@Validar", Types.INTEGER),
                        new SqlParameter("@HoraChequeo", Types.TIMESTAMP)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("@Justificacion", newJustificacion.justificacion);
        inParams.put("@Id_Usuario", 0);
        inParams.put("@Id_Chequeo", newJustificacion.idChequeo);
        inParams.put("@Validar", 2);
        inParams.put("@Resul", "");
        inParams.put("@bOk", 0);
        inParams.put("@RegistrosPagina", 0);
        inParams.put("@PaginaActual", 0);
        inParams.put("@HoraChequeo", LocalDateTime.now());


        Map<String, Object> result = jdbcCall.execute(inParams);

        DBResponse dbResponse = new DBResponse();
        dbResponse.Resul = ((String)result.get("Resul"));
        dbResponse.bOk = ((Integer)result.get("bOk"))  == 1 ? true : false;

        return dbResponse;
	}

}
