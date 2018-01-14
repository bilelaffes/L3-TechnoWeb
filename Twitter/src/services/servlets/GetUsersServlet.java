package services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
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

public class GetUsersServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String nomPrenom = req.getParameter("nomPrenom");
		JSONObject json = UserServices.getNomPrenomExist(nomPrenom);
		resp.setContentType("Text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json);
	}
}