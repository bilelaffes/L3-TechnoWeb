package bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class UserTools {

	/**
	 * 
	 * @param login
	 * @return boolean
	 * @throws SQLException
	 * @throws UserDoesntExistException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static boolean userExists(String login)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		boolean retour;
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT id from users WHERE login='" + login + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);

		if (rs.next()) {
			retour = true;
		} else {
			retour = false;
		}
		rs.close();
		st.close();
		conn.close();
		return retour;
	}

	/**
	 * 
	 * @param login
	 * @param password
	 * @param prenom
	 * @param nom
	 * @throws BDException
	 */
	public static void insertUser(String login, String password, String prenom, String nom, String mail) throws BDException {
		try {
			Connection conn = Database.getMySQLConnection();
			String query = "INSERT INTO users VALUES (null,'" + login + "','" + password + "','" + prenom + "','" + nom + "','" + mail 
					+ "')";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			st.close();
			conn.close();

		} catch (Exception e) {
			throw new BDException("Probleme avec insertUser" + e.getMessage());
		}
	}

	/**
	 * 
	 * @param login
	 * @param passWord
	 * @return boolean
	 * @throws WrongPasswordException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */

	public static boolean checkPassword(String login, String passWord)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		boolean passTrue = false;
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT password FROM users WHERE login='" + login + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		if (rs.next() && rs.getString("password").equals(passWord)) {
			passTrue = true;
		}
		return passTrue;
	}

	/**
	 * 
	 * @param login
	 * @return boolean
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public static int getIdUser(String login)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Connection conn = Database.getMySQLConnection();
		String query = "SELECT id FROM users WHERE login='" + login + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		int id = -1;
		if (rs.next()) {
			id = rs.getInt("id");
		}
		return id;
	}

	/**
	 * 
	 * @param login
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public static String getKey(String login)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		int id = getIdUser(login);
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT code FROM session WHERE id='" + id + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		String key = "";
		if (rs.next()) {
			key = rs.getString("key");
		}
		return key;
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int getIdUserSession(String key)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT id FROM session WHERE code='" + key + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		int id = -1;
		if (rs.next()) {
			id = rs.getInt("id");
		}
		return id;
	}

	/**
	 * 
	 * @param id
	 * @param nom
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String getPrenom(int id)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT prenom FROM users WHERE id='" + id + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		String nom = null;
		if (rs.next()) {
			nom = rs.getString("prenom");
		}
		return nom;
	}

	public static String getLogin(String prenom)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT login FROM users WHERE prenom='" + prenom + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		String login = null;
		if (rs.next()) {
			login = rs.getString("login");
		}
		return login;
	}

	public static String getLoginParId(int id)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String login = null;
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "SELECT login FROM users WHERE id='" + id + "'";
		ResultSet rs = st.executeQuery(query);
		if (rs.next()) {
			login = rs.getString("login");
		}
		return login;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean idExists(int id)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT id FROM users WHERE id='" + id + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean trouve = false;
		if (rs.next()) {
			trouve = true;
		}
		return trouve;
	}

	/**
	 * 
	 * @param id
	 * @param root
	 * @return key
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String insertSession(int id, boolean root)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		String key = UUID.randomUUID().toString();
		Connection conn = Database.getMySQLConnection();
		String query = "INSERT INTO session VALUES ('" + key + "','" + id + "',NOW(),false,false)";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
		return key;
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static boolean keyExists(String key)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT code from session WHERE code='" + key + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean trouve = false;
		if (rs.next() && rs.getString("code").equals(key)) {
			trouve = true;
		}
		return trouve;
	}

	/**
	 * 
	 * @param key
	 * @return boolean
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static boolean logout(String key)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = Database.getMySQLConnection();
		String query = "DELETE FROM session WHERE code='" + key + "'";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
		return true;
	}

    /**
     * 
     * @param nomPrenom
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
	public static JSONObject getNomPrenomExist(String nomPrenom)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		JSONObject json = new JSONObject();
		try {
			Connection conn = Database.getMySQLConnection();
			String query = "SELECT id,nom,prenom FROM users WHERE nom REGEXP '.*" +nomPrenom+ "' or prenom REGEXP '.*"+nomPrenom+"'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				json.accumulate("Users",rs.getInt("id") + " " +rs.getString("prenom") + " " + rs.getString("nom"));
			}
			rs.close();
			st.close();
			conn.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static String[] forgetPassword(String login)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String []tab = new String[2];
		Connection conn = Database.getMySQLConnection();
		String query = "SELECT mail,password from users WHERE login='" + login + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		if (rs.next()) {
			tab[0] = rs.getString("mail");
			tab[1] = rs.getString("password");
			return tab;
		}
		return null;
	} 
}
