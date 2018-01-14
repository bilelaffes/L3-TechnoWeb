package services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserServices;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class LogoutServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String key = req.getParameter("key");
		JSONObject json = UserServices.logout(key);
		
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie1 : cookies) {
				cookie1.setMaxAge(0);
				resp.addCookie(cookie1);
			}
		}
		
		resp.setContentType("Text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json);
	}
}