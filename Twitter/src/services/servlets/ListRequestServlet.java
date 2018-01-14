package services.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import bd.FriendsTools;


public class ListRequestServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		int id_from = Integer.parseInt(req.getParameter("id"));
		try {
			json = FriendsTools.listRequests(id_from);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		resp.setContentType("Text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json.toString());
	}

}
