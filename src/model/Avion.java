package model;

public class Avion {
	private int id;
	private String modelo;
	private int numeroAsientos;
	private int velocidadMaxima;
	private boolean activado;
	private Aeropuerto aeropuerto;
	
	public int getId() {
		return id;
	}
	public Avion setId(int id) {
		this.id = id;
		return this;
	}
	public String getModelo() {
		return modelo;
	}
	public Avion setModelo(String modelo) {
		this.modelo = modelo;
		return this;
	}
	public int getNumeroAsientos() {
		return numeroAsientos;
	}
	public Avion setNumeroAsientos(int numeroAsientos) {
		this.numeroAsientos = numeroAsientos;
		return this;
	}
	public int getVelocidadMaxima() {
		return velocidadMaxima;
	}
	public Avion setVelocidadMaxima(int velocidadMaxima) {
		this.velocidadMaxima = velocidadMaxima;
		return this;
	}
	public boolean isActivado() {
		return activado;
	}
	public Avion setActivado(boolean activado) {
		this.activado = activado;
		return this;
	}
	public Aeropuerto getAeropuerto() {
		return aeropuerto;
	}
	public Avion setAeropuerto(Aeropuerto aeropuerto) {
		this.aeropuerto = aeropuerto;
		return this;
	}
	@Override
	public String toString() {
		return modelo;
	}
	
	
	
}
