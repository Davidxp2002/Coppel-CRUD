package com.davidagm.coppelchecker.service;

import com.davidagm.coppelchecker.model.NewChequeo;
import com.davidagm.coppelchecker.model.DBResponse;
import com.davidagm.coppelchecker.model.DBResponseWithChecadas;
import com.davidagm.coppelchecker.model.NewJustificacion;
import com.davidagm.coppelchecker.model.PaginatorChequeos;

public interface IChequeosService {
	public DBResponse save(NewChequeo chequeo);
	public DBResponseWithChecadas findChequeosById(PaginatorChequeos paginatorChequeos);
	public DBResponse justificar(NewJustificacion newJustificacion);

}
