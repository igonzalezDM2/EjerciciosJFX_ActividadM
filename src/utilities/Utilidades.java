package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import enums.TipoAeropuerto;
import excepciones.AeropuertosException;
import model.Aeropuerto;
import model.Avion;
import model.Direccion;

public class Utilidades {
	
	private Utilidades() throws IllegalAccessException {
		throw new IllegalAccessException("Clase de utilidad");
	}
	
	public static Aeropuerto mapAeropuerto(ResultSet rs) throws AeropuertosException {
		try (InputStream is = rs.getBinaryStream("imagen")){
			Aeropuerto aeropuerto = new Aeropuerto()
					.setId(rs.getInt("id"))
					.setNombre(rs.getString("nombre"))
					.setAnioInauguracion(rs.getInt("ano"))
					.setCapacidad(rs.getInt("capacidad"))
					.setFinanciacion(rs.getDouble("financiacion"))
					.setNumTrabajadores(rs.getInt("numtrabajadores"))
					.setNumeroSocios(rs.getInt("numerosocios"))
					.setImagen(is != null ? is.readAllBytes() : null)
					.setDireccion(new Direccion()
							.setCalle(rs.getString("calle"))
							.setCiudad(rs.getString("ciudad"))
							.setId(rs.getInt("direccionid"))
							.setNumero(rs.getInt("numero"))
							.setPais(rs.getString("pais"))
							);
			if (rs.getInt("idpublico") > 0) {
				aeropuerto.setTipo(TipoAeropuerto.PUBLICO);
			} else if (rs.getInt("idprivado") > 0) {
				aeropuerto.setTipo(TipoAeropuerto.PRIVADO);
			}
			
			return aeropuerto;
			
		} catch (SQLException | IOException e) {
			throw new AeropuertosException(e);
		}
	}
	
	public static Avion mapAvion(ResultSet rs, Aeropuerto aeropuerto) throws AeropuertosException {
		try {
			return new Avion()
					.setActivado(rs.getBoolean("activado"))
					.setAeropuerto(aeropuerto)
					.setId(rs.getInt("id"))
					.setModelo(rs.getString("modelo"))
					.setNumeroAsientos(rs.getInt("numero_asientos"))
					.setVelocidadMaxima(rs.getInt("velocidad_maxima"));
					
		} catch (SQLException e) {
			throw new AeropuertosException(e);
		}
	}
	
	public static double parseDouble(String str) throws AeropuertosException {
		if (str != null && !str.isBlank()) {
			try {
				return Double.parseDouble(str.replace(',', '.'));
			} catch (NumberFormatException e) {/*QUE SALTE A LA EXCEPCIÓN*/}
		}
		throw new AeropuertosException("Formato de número decimal incorrecto");
	}
	
	public static int parseInt(String str) throws AeropuertosException {
		if (str != null && !str.isBlank()) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {/*QUE SALTE A LA EXCEPCIÓN*/}
		}
		throw new AeropuertosException("Formato de número entero incorrecto");
	}
	
	public static String infoAeropuerto(Aeropuerto aeropuerto, List<Avion> aviones) {
		Direccion direccion = aeropuerto.getDireccion();
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Nombre: %s\n", aeropuerto.getNombre()));
		sb.append(String.format("País: %s\n", direccion.getPais()));
		sb.append(String.format("Dirección: %s\n", direccion.getCalle()));
		sb.append(String.format("Año de inauguración: %d\n", aeropuerto.getAnioInauguracion()));
		sb.append(String.format("Capacidad: %d\n", aeropuerto.getCapacidad()));
		sb.append("Aviones:\n");
		for (Avion avion : aviones) {
			sb.append(String.format("\tModelo: %s\n", avion.getModelo()));			
			sb.append(String.format("\tNúmero de asientos: %d\n", avion.getNumeroAsientos()));			
			sb.append(String.format("\tVelocidad máxima: %d\n", avion.getVelocidadMaxima()));			
			sb.append(avion.isActivado() ? "\tActivado\n" : "\tDesactivado\n");	
		}
		TipoAeropuerto tipo = aeropuerto.getTipo();
		sb.append(tipo);
		if (TipoAeropuerto.PUBLICO.equals(tipo)){
			sb.append(String.format("Financiación: %,.2f\n", aeropuerto.getFinanciacion()));			
			sb.append(String.format("Número de trabajadores: %d\n", aeropuerto.getNumTrabajadores()));			
		} else if (TipoAeropuerto.PRIVADO.equals(tipo)) {
			sb.append(String.format("Número de socios: %d\n", aeropuerto.getNumeroSocios()));						
		}

		return sb.toString();
	}
	
}
