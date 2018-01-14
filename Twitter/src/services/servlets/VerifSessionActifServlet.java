package services.servlets;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifSessionActifServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			JSONObject json = new JSONObject();
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie cookie1 : cookies) {
					if (cookie1.getName().equals("login")) {
						json.put("login", cookie1.getValue());
					}
					else if (cookie1.getName().equals("id")) {
						json.put("id", cookie1.getValue());
					}else{
						json.put("ErreurCookieTrouve", 2);
					}
				}
				resp.setContentType("text/plain");
				resp.setHeader("Access-Control-Allow-Origin", "*");
				resp.getWriter().print(json);
			} else {
				json.put("CookieVide", 1);
				resp.setContentType("text/plain");
				resp.setHeader("Access-Control-Allow-Origin", "*");
				resp.getWriter().print(json);
			}
		} catch (JSONException e) {
			e.getMessage();
		}
	}
}
