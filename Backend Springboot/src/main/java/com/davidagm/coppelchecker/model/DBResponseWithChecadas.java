package com.davidagm.coppelchecker.model;

import java.util.List;

import lombok.Data;

@Data
public class DBResponseWithChecadas {
	public String Resul;
	public Boolean bOk;
	public List<Chequeo> checadas;
} 
