package services.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserServices;

public class PasseOublieServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		String login = req.getParameter("login");
		json = UserServices.passeOublie(login);
		resp.setContentType("text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json);
	}

}