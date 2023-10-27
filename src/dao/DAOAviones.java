package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import excepciones.AeropuertosException;
import model.Aeropuerto;
import model.Avion;
import utilities.Utilidades;



/**
 * La clase DAOAviones extiende DAOBase.
 */
public class DAOAviones extends DAOBase {
	
	/**
	 * Obtiene una lista de aviones de un aeropuerto específico.
	 * @param aeropuerto El aeropuerto del que se obtendrán los aviones.
	 * @return Una lista de aviones.
	 * @throws AeropuertosException Si ocurre un error al obtener los aviones.
	 */
	public static List<Avion> getAviones(Aeropuerto aeropuerto) throws AeropuertosException {
		try(Connection con = getConexion()) {
			StringBuilder sb = new StringBuilder("select "
					+ "aviones.id as id, "
					+ "aviones.modelo as modelo, "
					+ "aviones.numero_asientos as numero_asientos, "
					+ "aviones.velocidad_maxima as velocidad_maxima, "
					+ "aviones.activado as activado, "
					+ "aviones.id_aeropuerto as id_aeropuerto "
					+ "from aviones\n"
					+ "inner join aeropuertos on aeropuertos.id = aviones.id_aeropuerto\n"
					+ String.format("where aeropuertos.id = %d", aeropuerto.getId()));
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sb.toString());
			
			List<Avion> aviones = new LinkedList<>(); 
			
			while(rs.next()) {
				aviones.add(Utilidades.mapAvion(rs, aeropuerto));
			}
			
			return aviones;
			
		} catch (SQLException e) {
			throw new AeropuertosException(e);
		}
		
	}
	
	/**
	 * Añade un avión a la base de datos.
	 * @param avion El avión que se añadirá.
	 * @throws AeropuertosException Si los datos del avión son incompletos.
	 * @throws SQLException Si ocurre un error al añadir el avión.
	 */
	public static void anadirAvion(Avion avion) throws AeropuertosException, SQLException {
		if (avion != null && avion.getAeropuerto() != null && avion.getAeropuerto().getId() > 0) {
			
			String sqlDireccion = "INSERT INTO aviones (modelo, numero_asientos, velocidad_maxima, activado, id_aeropuerto) VALUES (?, ?, ?, ?, ?)";
			Connection con = null;
			int idAvion = 0;
			try {
				con = getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sqlDireccion, PreparedStatement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, avion.getModelo());
					ps.setInt(2, avion.getNumeroAsientos());
					ps.setInt(3, avion.getVelocidadMaxima());
					ps.setBoolean(4, avion.isActivado());
					ps.setInt(5, avion.getAeropuerto().getId());
					
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						idAvion = rs.getInt(1);
					}
				}
				
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				//ME PROTEJO EN CASO DE QUE UNA DE LAS INSERTS/UPDATES SALGA MAL, DESHACIENDO TODA LA TRANSACCIÓN
				con.rollback();
				throw new AeropuertosException(e);
			} finally {
				con.close();
			}
			
			//INSERTAR LOS IDS GENERADOS EN EL OBJETO PASADO COMO ARGUMENTO
			avion.setId(idAvion);
			
		} else {			
			throw new AeropuertosException("Los datos introducidos están incompletos");
		}
	}
	
	/**
	 * Modifica un avión en la base de datos.
	 * @param avion El avión que se modificará.
	 * @throws AeropuertosException Si los datos del avión son incompletos.
	 * @throws SQLException Si ocurre un error al modificar el avión.
	 */
	public static void modificarAvion(Avion avion) throws AeropuertosException, SQLException {
		if (avion != null && avion.getId() > 0) {
			
			String sqlDireccion = "UPDATE aviones SET modelo = ?, numero_asientos = ?, velocidad_maxima = ?, activado = ? , id_aeropuerto = ? WHERE id = ?";
			Connection con = null;
			try {
				con = getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sqlDireccion, PreparedStatement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, avion.getModelo());
					ps.setInt(2, avion.getNumeroAsientos());
					ps.setInt(3, avion.getVelocidadMaxima());
					ps.setBoolean(4, avion.isActivado());
					ps.setInt(5, avion.getAeropuerto().getId());
					ps.setInt(6, avion.getId());
					
					ps.executeUpdate();
				}
				
				con.commit();
			} catch (SQLException e) {
				
				e.printStackTrace();
				//ME PROTEJO EN CASO DE QUE UNA DE LAS INSERTS/UPDATES SALGA MAL, DESHACIENDO TODA LA TRANSACCIÓN
				con.rollback();
				throw new AeropuertosException(e);
			} finally {
				con.close();
			}
			
		} else {			
			throw new AeropuertosException("Los datos introducidos están incompletos");
		}
	}
	
	/**
	 * Borra un avión de la base de datos.
	 * @param avion El avión que se borrará.
	 * @throws SQLException Si ocurre un error al borrar el avión.
	 * @throws AeropuertosException Si el avión es nulo o su ID es inválido.
	 */
	public static void borrarAvion(Avion avion) throws SQLException, AeropuertosException {
		if (avion == null || avion.getId() < 0) {
			return;
		}
		String sql = "DELETE FROM aviones WHERE id = ?";
		Connection con = null;
		try {
			con = getConexion();
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, avion.getId());
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw new AeropuertosException(e);
		} finally {
			con.close();
		}
		
	}
	
	public static void checkExiste(Avion avion) throws AeropuertosException {
		Aeropuerto aeropuerto = avion.getAeropuerto();
		String modelo = avion.getModelo();
		if (aeropuerto != null && aeropuerto.getId() > 0 && modelo != null && !modelo.isBlank()) {
			try (Connection con = getConexion()) {
				String sql = "SELECT id FROM aviones WHERE modelo = ? AND id_aeropuerto = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, modelo);
				ps.setInt(2, aeropuerto.getId());
				ResultSet rs = ps.executeQuery();
				if (rs.first()) {
					throw new SQLException("Ya existe un avión del mismo modelo con el mismo aeropuerto");
				}
				
			} catch (SQLException e) {
				throw new AeropuertosException(e);
			}
		}
	}
	
	
	
}
