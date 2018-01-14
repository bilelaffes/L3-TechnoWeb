package services.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import bd.FriendsTools;
import bd.UserTools;

public class FriendRequestServlet extends HttpServlet  {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		int id = Integer.parseInt(req.getParameter("id"));
		int id_to = Integer.parseInt(req.getParameter("id_to"));
		try {
			boolean b = FriendsTools.friendRequest(id, id_to);
			if (b == true){
				json.put("Ajout", 1);
			}else{
				json.put("IdDoesntExist", -1);
			}
			resp.setContentType("Text/plain");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.getWriter().print(json);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
