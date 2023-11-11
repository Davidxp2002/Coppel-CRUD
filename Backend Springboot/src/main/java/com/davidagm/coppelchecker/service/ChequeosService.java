package com.davidagm.coppelchecker.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidagm.coppelchecker.model.NewChequeo;
import com.davidagm.coppelchecker.model.DBResponse;
import com.davidagm.coppelchecker.model.DBResponseWithChecadas;
import com.davidagm.coppelchecker.model.NewJustificacion;
import com.davidagm.coppelchecker.model.PaginatorChequeos;
import com.davidagm.coppelchecker.repository.IChequeosRepository;

@Service
public class ChequeosService implements IChequeosService{

	@Autowired
	private IChequeosRepository iChequeosRepository;
	
	@Override
	public DBResponse save(NewChequeo chequeo) {
		DBResponse dbResponse;
		try {
			dbResponse = iChequeosRepository.save(chequeo);
		}catch(Exception e) {
			throw e;
		}
		return dbResponse;
	}

	@Override
	public DBResponseWithChecadas findChequeosById(PaginatorChequeos paginatorChequeos) {
		DBResponseWithChecadas dbResponse = new DBResponseWithChecadas(); 
		try {
			dbResponse = iChequeosRepository.findChequeosById(paginatorChequeos);
		}catch(Exception e) {
			throw e;
		}
		return dbResponse;
	}

	@Override
	public DBResponse justificar(NewJustificacion newJustificacion) {
		DBResponse dbResponse;
		try {
			dbResponse = iChequeosRepository.justificar(newJustificacion);
		}catch(Exception e) {
			throw e;
		}
		return dbResponse;
	}

}
