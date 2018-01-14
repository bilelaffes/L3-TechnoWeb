package services.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserServices;

public class AddCommentListServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		int id = Integer.parseInt(req.getParameter("id"));
		String login = req.getParameter("loginTab");
		String commentaire = req.getParameter("commentaire");
		String [] loginArray = login.split(",");
		json = UserServices.insertCommentList(id,loginArray, commentaire);
		resp.setContentType("text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json);
	}

}
