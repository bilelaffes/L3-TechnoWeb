package services.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import bd.UserTools;
import services.UserServices;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class LoginServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String login = req.getParameter("login");
			String password = req.getParameter("pass");
			JSONObject json = new JSONObject();

			json = UserServices.getID(login);

			if (UserTools.userExists(login) && UserTools.checkPassword(login, password)){
				Cookie cookie = new Cookie("login", login);
				Cookie cookie2 = new Cookie("id", json.getString("id"));
				cookie.setMaxAge(60 * 60 * 24);
				resp.addCookie(cookie);
				resp.addCookie(cookie2);
			}

			json = UserServices.login(login, password);

			resp.setContentType("Text/plain");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.getWriter().print(json);
		} catch (JSONException | InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.getMessage();
		}
	}
}