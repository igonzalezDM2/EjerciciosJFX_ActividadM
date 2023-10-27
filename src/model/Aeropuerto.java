package model;

import enums.TipoAeropuerto;

public class Aeropuerto {
	private int id;
	private String nombre;
	private int anioInauguracion;
	private int capacidad;
	private Direccion direccion;
	private byte[] imagen;
	
	//PROPIEDADES AJENAS A LA TABLA PRINCIPAL
	private TipoAeropuerto tipo;
	private int numeroSocios;
	private double financiacion;
	private int numTrabajadores;
	
	
	
	
	public int getId() {
		return id;
	}
	public Aeropuerto setId(int id) {
		this.id = id;
		return this;
	}
	public String getNombre() {
		return nombre;
	}
	public Aeropuerto setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}
	public int getAnioInauguracion() {
		return anioInauguracion;
	}
	public Aeropuerto setAnioInauguracion(int anioInauguracion) {
		this.anioInauguracion = anioInauguracion;
		return this;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public Aeropuerto setCapacidad(int capacidad) {
		this.capacidad = capacidad;
		return this;
	}
	public Direccion getDireccion() {
		return direccion;
	}
	public Aeropuerto setDireccion(Direccion direccion) {
		this.direccion = direccion;
		return this;
	}
	public byte[] getImagen() {
		return imagen;
	}
	public Aeropuerto setImagen(byte[] imagen) {
		this.imagen = imagen;
		return this;
	}
	
	public TipoAeropuerto getTipo() {
		return tipo;
	}
	public Aeropuerto setTipo(TipoAeropuerto tipo) {
		this.tipo = tipo;
		return this;
	}
	/**
	 * PROPIEDAD DE AEROPUERTO PRIVADO
	 */
	public int getNumeroSocios() {
		return numeroSocios;
	}
	/**
	 * PROPIEDAD DE AEROPUERTO PRIVADO
	 */
	public Aeropuerto setNumeroSocios(int numeroSocios) {
		this.numeroSocios = numeroSocios;
		return this;
	}
	/**
	 * PROPIEDAD DE AEROPUERTO PÚBLICO
	 */
	public double getFinanciacion() {
		return financiacion;
	}
	/**
	 * PROPIEDAD DE AEROPUERTO PÚBLICO
	 */
	public Aeropuerto setFinanciacion(double financiacion) {
		this.financiacion = financiacion;
		return this;
	}
	/**
	 * PROPIEDAD DE AEROPUERTO PÚBLICO
	 */
	public int getNumTrabajadores() {
		return numTrabajadores;
	}
	/**
	 * PROPIEDAD DE AEROPUERTO PÚBLICO
	 */
	public Aeropuerto setNumTrabajadores(int numTrabajadores) {
		this.numTrabajadores = numTrabajadores;
		return this;
	}
	
	@Override
	public String toString() {
		return nombre;
	}
	
}
