package services.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import bd.FriendsTools;

public class RefuseFriendServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		int id = Integer.parseInt(req.getParameter("id"));
		int id_to = Integer.parseInt(req.getParameter("id_to"));
		try {
			if (FriendsTools.refuseFriend(id, id_to) == true){
				json.put("refuse", 1);
				if (FriendsTools.removeRequest(id, id_to) == true)
					json.put("requestremoved", 2);
			}else{
				json.put("probleme", -1);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException | JSONException e) {
			e.printStackTrace();
		}
		resp.setContentType("Text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json);
	}

}
