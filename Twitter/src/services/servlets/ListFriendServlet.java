package services.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import bd.FriendsTools;
import bd.UserTools;
import services.UserServices;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 *
 */
public class ListFriendServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		int id_from = Integer.parseInt(req.getParameter("id_from"));
		json = UserServices.listFriend(id_from);
		resp.setContentType("Text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json.toString());
	}
}
