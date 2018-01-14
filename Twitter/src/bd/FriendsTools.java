package bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class FriendsTools {
	/**
	 * 
	 * @param key
	 * @param id_friend
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static boolean insertFriend(int id_from, int id_friend)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		boolean b = false;
		if (friendAccepted(id_friend, id_from) == true){
			Connection conn = Database.getMySQLConnection();
			Statement st = conn.createStatement();
			String query1 = "INSERT INTO friends VALUES("+ id_from +", "+ id_friend +", NOW())";
			String query2 = "INSERT INTO friends VALUES("+ id_friend +", "+ id_from +", NOW())";
			st.executeUpdate(query1);
			st.executeUpdate(query2);
			st.close();
			conn.close();
			b = true;
		}
		if(friendRefused(id_friend, id_from) == true){
			b = false;
		}
		removeRequest(id_friend, id_from);
		return b;
	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean friendRequest(int id_from, int id_to) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if (UserTools.idExists(id_from) == false || UserTools.idExists(id_to) == false){
			return false;
		}else{
			Connection conn = Database.getMySQLConnection();
			Statement st = conn.createStatement();
			String query = "INSERT INTO friendsRequest VALUES ('"+id_from+"','"+id_to+"','sent')";
			st.executeUpdate(query);
			st.close();
			conn.close();
			return true;
		}
	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean friendRequestExists(int id_from, int id_to) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		boolean trouve = false;
		if (UserTools.idExists(id_from) == false || UserTools.idExists(id_to) == false){
			return false;
		}else{
			Connection conn = Database.getMySQLConnection();
			Statement st = conn.createStatement();
			String query = "SELECT * FROM friendsRequest WHERE envoyeur='"+id_from+"' AND "+
					"receveur='"+id_to+"'";
			ResultSet rs = st.executeQuery(query);
			if (rs.next()){
				trouve = true;
			}
			st.close();
			conn.close();
			return trouve;
		}
	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean acceptFriend(int id_from, int id_to) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if (UserTools.idExists(id_from) == false || UserTools.idExists(id_to) == false || friendRequestExists(id_to, id_from) == false){
			return false;
		}
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "UPDATE friendsRequest SET statut='Accepted' WHERE envoyeur='"+id_to+
				"' AND receveur='"+id_from+"'";
		st.executeUpdate(query);
		st.close();
		conn.close();
		return true;

	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean friendAccepted(int id_from, int id_to) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		boolean trouve = false;
		if (UserTools.idExists(id_from) == false || UserTools.idExists(id_to) == false){
			return false;
		}
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "SELECT statut FROM friendsRequest WHERE envoyeur='"+id_from+
					"' AND receveur='"+id_to+"'";
		ResultSet rs = st.executeQuery(query);
		if (rs.next() && rs.getString("statut").equals("Accepted")){
			trouve = true;
		}
		st.close();
		conn.close();
		return trouve;
		
	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean refuseFriend(int id_from, int id_to) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if (UserTools.idExists(id_from) == false || UserTools.idExists(id_to) == false || friendRequestExists(id_to, id_from) == false){
			return false;
		}
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "UPDATE friendsRequest SET statut='Refused' WHERE envoyeur='"+id_to+
				"' AND receveur='"+id_from+"'";
		st.executeUpdate(query);
		st.close();
		conn.close();
		return true;

	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean removeRequest(int id_from, int id_to) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if (UserTools.idExists(id_from) == false || UserTools.idExists(id_to) == false){
			return false;
		}
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "DELETE FROM friendsRequest WHERE envoyeur='"+id_to+"' AND receveur='"+id_from+"'";
		st.executeUpdate(query);
		st.close();
		conn.close();
		return true;
	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean friendRefused(int id_from, int id_to) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		boolean trouve = false;
		if (UserTools.idExists(id_from) == false || UserTools.idExists(id_to) == false){
			return false;
		}
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "SELECT statut FROM friendsRequest WHERE envoyeur='"+id_from+
					"' AND receveur='"+id_to+"'";
		ResultSet rs = st.executeQuery(query);
		if (rs.next() && rs.getString("statut").equals("Refused")){
			trouve = true;
		}
		st.close();
		conn.close();
		return trouve;
		
	}
	/**
	 * 
	 * @param id_from
	 * @param id_to
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */

	public static boolean deleteFriend(int id_from, int id_to)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query1 = "DELETE FROM friends WHERE de='" + id_from + "'AND pour='" + id_to + "'";
		String query2 = "DELETE FROM friends WHERE de='" + id_to + "'AND pour='" + id_from + "'";
		st.executeUpdate(query1);
		st.executeUpdate(query2);
		st.close();
		conn.close();
		return true;

	}

	/**
	 * 
	 * @param id_from
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static ArrayList<Integer> recupID(int id_from)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		ArrayList<Integer> list = new ArrayList<>();
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "SELECT pour FROM friends WHERE de='" + id_from + "'";
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			int to = rs.getInt("pour");
			list.add(to);
		}
		st.close();
		conn.close();
		return list;
	}
/**
 * 
 * @param id_from
 * @return
 * @throws InstantiationException
 * @throws IllegalAccessException
 * @throws ClassNotFoundException
 * @throws SQLException
 * @throws JSONException
 */
	public static JSONObject listFriend(int id_from)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, JSONException {
		ArrayList<Integer> list = recupID(id_from);
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		JSONObject json = new JSONObject();
		ArrayList<String>friends = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String query = "SELECT nom,prenom FROM users WHERE id='" + list.get(i) + "'";
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				String prenom = rs.getString("prenom");
				friends.add(prenom);
			}
		}
		json.accumulate("Friends", friends);
		System.out.println(json);
		st.close();
		conn.close();
		return json;
	}
	/**
	 * 
	 * @param id_from
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static JSONObject listRequests(int id_from) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		ArrayList<Integer> liste = new ArrayList<Integer>();
		JSONObject json = new JSONObject();
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String query = "SELECT envoyeur FROM friendsRequest WHERE receveur='"+id_from+"' AND statut='sent'";
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			int id = rs.getInt("envoyeur");
			liste.add(id);
		}
		try {
			json.accumulate("ajouts", liste);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		st.close();
		conn.close();
		return json;
	}
	
	public static ArrayList<String> listFriend2(int id_from)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, JSONException {
		ArrayList<Integer> list = recupID(id_from);
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		ArrayList<String>friends = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String query = "SELECT nom,prenom FROM users WHERE id='" + list.get(i) + "'";
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				String prenom = rs.getString("prenom");
				friends.add(prenom);
			}
		}
		st.close();
		conn.close();
		return friends;
	}
}
