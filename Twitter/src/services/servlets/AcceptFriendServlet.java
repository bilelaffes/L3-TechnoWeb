package services.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import bd.FriendsTools;

public class AcceptFriendServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		int id = Integer.parseInt(req.getParameter("id"));
		int id_to = Integer.parseInt(req.getParameter("id_to"));
		try {
			if (FriendsTools.acceptFriend(id, id_to) == true){
				json.put("accepte", 1);
				if (FriendsTools.insertFriend(id, id_to) == true);
					json.put("amitie", 2);	
			}else{
				json.put("probleme", -1);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		resp.setContentType("Text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json);

	}

}
