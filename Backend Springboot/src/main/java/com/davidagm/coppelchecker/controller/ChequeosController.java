package com.davidagm.coppelchecker.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davidagm.coppelchecker.model.NewChequeo;
import com.davidagm.coppelchecker.model.Chequeo;
import com.davidagm.coppelchecker.model.DBResponse;
import com.davidagm.coppelchecker.model.DBResponseWithChecadas;
import com.davidagm.coppelchecker.model.NewJustificacion;
import com.davidagm.coppelchecker.model.PaginatorChequeos;
import com.davidagm.coppelchecker.model.ServiceResponse;
import com.davidagm.coppelchecker.model.ServiceResponseWithChecadas;
import com.davidagm.coppelchecker.service.IChequeosService;

@RestController
@RequestMapping("api/v1/chequeos")
@CrossOrigin("*")
public class ChequeosController {

	@Autowired
	private IChequeosService iChequeosService;
	
	@PostMapping("/save")
	public ResponseEntity<ServiceResponse> save(@RequestBody NewChequeo chequeo){
		ServiceResponse serviceResponse =  new ServiceResponse();
		DBResponse dbResponse = iChequeosService.save(chequeo);
		
		serviceResponse.success = dbResponse.bOk;
		serviceResponse.message = dbResponse.Resul;

		return new ResponseEntity<>(serviceResponse, HttpStatus.CREATED);
	}
	
	@PutMapping("/justificar")
	public ResponseEntity<ServiceResponse> justificar(@RequestBody NewJustificacion newJustificacion){
		ServiceResponse serviceResponse =  new ServiceResponse();
		DBResponse dbResponse = iChequeosService.justificar(newJustificacion);
		
		serviceResponse.success = dbResponse.bOk;
		serviceResponse.message = dbResponse.Resul;

		return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
	}
	
	@PostMapping("/getById")
	public ResponseEntity<ServiceResponseWithChecadas> justificar(@RequestBody PaginatorChequeos paginatorChequeos){
		ServiceResponseWithChecadas serviceResponse =  new ServiceResponseWithChecadas();
		DBResponseWithChecadas dbResponse  = iChequeosService.findChequeosById(paginatorChequeos);
		
	
		serviceResponse.success = dbResponse.bOk;
		serviceResponse.message = dbResponse.Resul;
		serviceResponse.checadas = dbResponse.checadas;

		return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
	}
}
