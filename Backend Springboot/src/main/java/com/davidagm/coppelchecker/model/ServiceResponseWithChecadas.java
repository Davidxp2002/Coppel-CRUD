package com.davidagm.coppelchecker.model;

import java.util.List;

import lombok.Data;

@Data
public class ServiceResponseWithChecadas {
	public Boolean success;
	public String message;
	public List<Chequeo> checadas;
	
}

