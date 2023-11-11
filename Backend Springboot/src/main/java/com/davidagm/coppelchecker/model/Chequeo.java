package com.davidagm.coppelchecker.model;

import java.util.Date;

import lombok.Data;

@Data
public class Chequeo {
	public Long id;
	public String tipo;
	public String estatus;
	public Date horachequeo;
	public String justificacion;
	public int idUsuario;
	public int registros;
	public String nombreUsuario;
}
