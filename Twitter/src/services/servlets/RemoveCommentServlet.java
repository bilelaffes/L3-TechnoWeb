package services.servlets;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoException;

import services.UserServices;
import bd.CommentsTools;

public class RemoveCommentServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		String id = req.getParameter("id");
		String login = req.getParameter("login");
		String commentaire = req.getParameter("commentaire");
		json = UserServices.removeComment(Integer.parseInt(id),login,commentaire);
		resp.setContentType("text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		try {
			json.put("commentaire",commentaire);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.getWriter().print(json);
	}

}
