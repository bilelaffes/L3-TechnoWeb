package services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import bd.BDException;
import services.UserServices;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class CreateUserServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		String nom = req.getParameter("nom");
		String prenom = req.getParameter("prenom");
		String mail = req.getParameter("mail");
		JSONObject json = UserServices.createUser(login, password, nom, prenom, mail);
		resp.setContentType("Text/plain");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().print(json);
	}
}