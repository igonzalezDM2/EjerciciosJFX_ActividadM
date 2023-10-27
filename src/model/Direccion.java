package model;

import java.io.Serializable;

public class Direccion implements Serializable {
	
	private static final long serialVersionUID = -2564882302215063430L;
	
	private int id;
	private String pais;
	private String ciudad;
	private String calle;
	private int numero;
	
	
	public int getId() {
		return id;
	}
	public Direccion setId(int id) {
		this.id = id;
		return this;
	}
	public String getPais() {
		return pais;
	}
	public Direccion setPais(String pais) {
		this.pais = pais;
		return this;
	}
	public String getCiudad() {
		return ciudad;
	}
	public Direccion setCiudad(String ciudad) {
		this.ciudad = ciudad;
		return this;
	}
	public String getCalle() {
		return calle;
	}
	public Direccion setCalle(String calle) {
		this.calle = calle;
		return this;
	}
	public int getNumero() {
		return numero;
	}
	public Direccion setNumero(int numero) {
		this.numero = numero;
		return this;
	}
	
}
