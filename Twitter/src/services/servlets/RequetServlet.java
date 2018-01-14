package services.servlets;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import bd.BDException;
import bd.Database;
import bd.UserTools;
import services.UserServices;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class RequetServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			JSONObject json = new JSONObject();
			JSONObject json2 = new JSONObject();
			JSONObject json3 = new JSONObject();
			JSONObject json4 = new JSONObject();
			JSONObject json5 = new JSONObject();
			
			Database.createTableFriendsRequest();
			/*Database.createTableUsers();
			Database.createTableSession();
			Database.createTableFriends();*/

			UserServices.createUser("chef", "bilYous", "benchadi", "bilel", "bilel@hotmail.com");
			String key = UserTools.insertSession(UserTools.getIdUser("chef"), true);
			UserServices.addFriend(key, 3);
			resp.setContentType("Text/plain");
			
			Connection connection = Database.getMySQLConnection();
			String query = "SELECT * FROM users";
			String query2 = "SELECT * FROM session";
			String query3 = "SELECT * FROM friends";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				String id = rs.getString("id");
				String login = rs.getString("login");
				json.put("login", login);
				json.put("id", id);
				resp.getWriter().println(json);
			}
			rs = st.executeQuery(query2);
			while (rs.next()) {
				String id = rs.getString("code");
				String login = rs.getString("root");
				json2.put("code", id);
				json2.put("root", login);
				resp.getWriter().println(json2);
			}
			rs = st.executeQuery(query3);
			while (rs.next()) {
				String id = rs.getString("de");
				String id2 = rs.getString("pour");
				json3.put("de", id);
				json3.put("pour", id2);
				resp.getWriter().println(json3);
			}
			
			json5.put("*************************************", 1);
			resp.getWriter().println(json5);
			
			UserServices.removeFriend(key, 3);
			rs = st.executeQuery(query3);
			while (rs.next()) {
				String id = rs.getString("de");
				String id2 = rs.getString("pour");
				json4.put("de", id);
				json4.put("pour", id2);
				resp.getWriter().println(json4);
			}
			rs.close();
			st.close();
			connection.close();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException |JSONException  e) {
			e.printStackTrace();
		}
	}
}

