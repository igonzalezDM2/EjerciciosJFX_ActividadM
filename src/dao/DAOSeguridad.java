package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import excepciones.AeropuertosException;

/**
 * La clase DAOSeguridad extiende DAOBase.
 */
public class DAOSeguridad extends DAOBase {
	
	/**
	 * Realiza el inicio de sesión del usuario.
	 * @param user El nombre de usuario.
	 * @param pass La contraseña del usuario.
	 * @throws AeropuertosException Si las credenciales son incorrectas o si no se puede realizar la conexión a la base de datos.
	 */
	public static void login(String user, String pass) throws AeropuertosException {
		try (Connection con = getConexion()) {
			String sql = "SELECT usuario, password FROM usuarios WHERE usuario = ? AND password = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			if (!rs.first()) {
				throw new AeropuertosException("Las credenciales proporcionadas son incorrectas");
			}
		} catch (SQLException e) {
			throw new AeropuertosException("No se pudo realizar la conexión a BD", e);
		}
	}
}
