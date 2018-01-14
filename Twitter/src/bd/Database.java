package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
	private DataSource dataSource;
	private static Database database;

	public Database(String jndiname) throws SQLException {
		try {
			dataSource = (DataSource) new InitialContext()
					.lookup("java:comp/env/" + jndiname);
		} catch (NamingException e) {
			throw new SQLException(jndiname + " is missing in JNDI :"
					+ e.getMessage());
		}
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public static Connection getMySQLConnection()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		if (DBStatic.mysql_pooling == false) {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String n = "jdbc:mysql://" + DBStatic.mysql_host + "/"
					+ DBStatic.mysql_db;
			return DriverManager.getConnection(n, DBStatic.mysql_username,
					DBStatic.mysql_password);
		} else {
			if (database == null)
				database = new Database("jdbc/db");
			return (database.getConnection());
		}
	}

	public static void createTableUsers() throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = getMySQLConnection();
		String query = "CREATE TABLE users " +
				"(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
				" login VARCHAR(32) UNIQUE, " + 
				" password VARCHAR(32), " + 
				" nom VARCHAR(255), " + 
				" prenom VARCHAR(255))"; 
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
	}

	public static void createTableSession() throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, SQLException {

		Connection conn = getMySQLConnection();
		String query = "CREATE TABLE session " +
				"(code VARCHAR(36) PRIMARY KEY, " +
				" id INTEGER, " + 
				" timestamp TIMESTAMP, " + 
				" root BOOLEAN, " + 
				" expired BOOLEAN)";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
	}

	public static void createTableFriends() throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, SQLException {

		Connection conn = getMySQLConnection();
		String query = "CREATE TABLE friends " +
				"(de INTEGER, " +
				" pour INTEGER, " + 
				" time TIMESTAMP, " + 
				" PRIMARY KEY(de, pour))";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
	}

	public static void createTableFriendsRequest() {
		try{
			Connection conn = getMySQLConnection();
			String query = "CREATE TABLE friendsRequest " +
					"(envoyeur INTEGER, " + 
					"receveur INTEGER, " + 
					"statut VARCHAR(36), "+
					"PRIMARY KEY(envoyeur, receveur))";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			st.close();
			conn.close();
		}catch(InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e){
			e.getMessage();
		}
	}

}
