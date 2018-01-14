package services;

import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONException;
import org.json.JSONObject;

import bd.BDException;
import bd.CommentsTools;
import bd.DBStatic;
import bd.FriendsTools;
import bd.UserTools;
import bd.WrongPasswordException;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class UserServices {
	/**
	 * 
	 * @param login
	 * @param password
	 * @param nom
	 * @param prenom
	 * @return json
	 * @throws BDException
	 */

	public static JSONObject createUser(String login, String password, String nom, String prenom,String mail) {
		if (login == null || password == null || nom == null || prenom == null || mail == null)
			return ServicesTools.serviceRefused("PbArgument", -1);
		try {
			if (UserTools.userExists(login))
				return ServicesTools.serviceRefused("LoginExistant", 2);
			UserTools.insertUser(login, password, prenom, nom, mail);
		} catch (SQLException e) {
			e.getMessage();
		} catch (InstantiationException e) {
			e.getMessage();
		} catch (IllegalAccessException e) {
			e.getMessage();
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} catch (BDException e) {
			e.printStackTrace();
		}
		return ServicesTools.serviceAccepted();
	}

	/**
	 * 
	 * @param login
	 * @param passWord
	 * @return json
	 */

	public static JSONObject login(String login, String password) {

		try {
			if (login == null || password == null)
				return ServicesTools.serviceRefused("PbArgument", -1);

			if (!UserTools.userExists(login) && !UserTools.checkPassword(login, password)) {
				return ServicesTools.serviceRefused("LoginOuPassWordIcompatible", 15);
			}

			if (!UserTools.userExists(login)) {
				return ServicesTools.serviceRefused("LoginInexistant", 3);
			}
			if (!UserTools.checkPassword(login, password)) {
				return ServicesTools.serviceRefused("PasswordIncorrect", 4);
			}

			int id = UserTools.getIdUser(login);
			boolean root;
			if (login.equals(DBStatic.mysql_username))
				root = true;
			root = false;
			String key = UserTools.insertSession(id, root);
			JSONObject json = new JSONObject();
			json.put("key", key);
			json.put("id", id);
			return json;

		} catch (JSONException e) {
			return ServicesTools.serviceRefused("Erreur JSON", 100);

		} catch (SQLException e) {
			return ServicesTools.serviceRefused("Erreur SQL", 1000);
		} catch (InstantiationException e) {
			return ServicesTools.serviceRefused("Erreur InstantiationException ", 1000);
		} catch (IllegalAccessException e) {
			return ServicesTools.serviceRefused("Erreur IllegalAccessException ", 1000);
		} catch (ClassNotFoundException e) {
			return ServicesTools.serviceRefused("Erreur ClassNotFoundException ", 1000);
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */

	public static JSONObject logout(String key) {
		try {
			if (key == null)
				return ServicesTools.serviceRefused("Key est a null", 5);

			if (!UserTools.keyExists(key)) {
				return ServicesTools.serviceRefused("Key n'existe pas", 6);
			} else {
				UserTools.logout(key);
			}
		} catch (SQLException e) {
			e.getMessage();
		} catch (InstantiationException e) {
			e.getMessage();
		} catch (IllegalAccessException e) {
			e.getMessage();
		} catch (ClassNotFoundException e) {
			e.getMessage();
		}
		return ServicesTools.serviceAccepted();
	}

	/**
	 * 
	 * @param key
	 * @param id_friend
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static JSONObject addFriend(String key, int id_friend) {
		boolean a = false;
		if (key == null) {
			return ServicesTools.serviceRefused("PbArgument", -1);
		}
		try {
			if (UserTools.keyExists(key) == false) {
				return ServicesTools.serviceRefused("key doesn't exists", 7);
			} else {
				int id_from = UserTools.getIdUserSession(key);
				a = FriendsTools.insertFriend(id_from, id_friend);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.getMessage();
		}
		if (a == true)
			return ServicesTools.serviceAccepted();
		return ServicesTools.serviceRefused("Ami a refuse votre demande", 8);
	}

	/**
	 * 
	 * @param key
	 * @param id_friend
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static JSONObject removeFriend(String key, int id_friend) {
		JSONObject json = new JSONObject();

		if (key == null) {
			return ServicesTools.serviceRefused("PbArgument", -1);
		}
		try {
			if (UserTools.keyExists(key) == false) {
				return ServicesTools.serviceRefused("KeyDoesntExist", 7);
			} else {
				int id_from = UserTools.getIdUserSession(key);
				FriendsTools.deleteFriend(id_from, id_friend);
				json.put("amiSup", 1);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException
				| JSONException e) {
			e.getMessage();
		}
		return json;
	}

	public static JSONObject listFriend(int id_from) {
		JSONObject json = null;
		try {
			if (UserTools.idExists(id_from) == false)
				return ServicesTools.serviceRefused("cetIdNexistePas", 8);
			else {
				json = FriendsTools.listFriend(id_from);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException
				| JSONException e) {
			e.printStackTrace();
		}
		return json;

	}

	public static JSONObject addComment(int id, String commentaire) {
		if (commentaire == null) {
			return ServicesTools.serviceRefused("PbArgument", -1);
		} else {
			GregorianCalendar calendar = new java.util.GregorianCalendar();
			Date date = calendar.getTime();
			boolean b = CommentsTools.insertComment(id, date, commentaire);
			if (b == false) {
				return ServicesTools.serviceRefused("idOuNomExistePas", 9);
			}
			return ServicesTools.serviceAccepted();
		}
	}

	public static JSONObject removeComment(int id, String login, String commentaire) {
		if (commentaire == null) {
			return ServicesTools.serviceRefused("PbArgument", -1);
		} else {
			boolean b = CommentsTools.removeComment(id, login, commentaire);
			if (b == false) {
				return ServicesTools.serviceRefused("KeyNexistePas", 10);
			}
			return ServicesTools.serviceAccepted();
		}
	}

	public static JSONObject getID(String login) {
		JSONObject json = new JSONObject();
		try {
			if (login == null) {
				return ServicesTools.serviceRefused("PbArgument", -1);
			} else {
				int id = UserTools.getIdUser(login);
				if (id == -1) {
					return ServicesTools.serviceRefused("idOuNomExistePas", 9);
				}
				json.put("id", id);
				json.put("OK", 200);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException
				| JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject insertCommentList(int id, String[] tabLogin, String commentaire) {
		JSONObject json = new JSONObject();
		try {
			int i = 0;
			if (tabLogin == null || commentaire == null) {
				return ServicesTools.serviceRefused("PbArgument", -1);
			} else {
				for (i = 0; i < tabLogin.length; i++) {
					int id_log = UserTools.getIdUser(tabLogin[i]);
					if (id_log == -1) {
						return ServicesTools.serviceRefused("idOuNomExistePas", 9);
					}
					GregorianCalendar calendar = new java.util.GregorianCalendar();
					Date date = calendar.getTime();
					json = CommentsTools.insertCommentaire(id, id_log, date, commentaire);
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject getCommentList() {
		JSONObject json = new JSONObject();
		json = CommentsTools.getCommentAll();
		return json;
	}

	public static JSONObject getNomPrenomExist(String nomPrenom) {
		JSONObject json = new JSONObject();
		if (nomPrenom == null) {
			return ServicesTools.serviceRefused("PbArgument", -1);
		} else {
			try {
				json = UserTools.getNomPrenomExist(nomPrenom);
				if (json.length() == 0) {
					return ServicesTools.serviceRefused("nomPrenomInexistant", 15);
				} else {
					json.put("OK", 200);
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException
					| JSONException e) {
				e.printStackTrace();
			}
			return json;
		}
	}
	
	public static JSONObject passeOublie(String login){
		String [] mail;
		if(login == null){
			return ServicesTools.serviceRefused("PbArgument", -1);
		}else{
			try {
				mail = UserTools.forgetPassword(login);
				if(mail == null){
					return ServicesTools.serviceRefused("aucunLogin",40);
				}
				sendMessage("PasseOublié",mail[1], mail[0]);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ServicesTools.serviceAccepted();
		}
	}

	public static void sendMessage(String subject, String text, String destinataire) {
		final ResourceBundle smtpBundle = ResourceBundle.getBundle("domaines.proprties.smtp");

		// smtp properties
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpBundle.getString("mail.smtp.host"));
		props.put("mail.smtp.socketFactory.port", smtpBundle.getString("mail.smtp.socketFactory.port"));
		props.put("mail.smtp.socketFactory.class", smtpBundle.getString("mail.smtp.socketFactory.class"));
		props.put("mail.smtp.auth", smtpBundle.getString("mail.smtp.auth"));
		props.put("mail.smtp.port", smtpBundle.getString("mail.smtp.port"));

		// authentification

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(smtpBundle.getString("mail.session.user"),
						smtpBundle.getString("mail.session.pass"));
			}
		});

		// construct message
		Message message = new MimeMessage(session);
		try{
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
			message.setSubject(subject);
			message.setContent(" Votre mot de passe est : "+text, "text/plain; charset=ISO-8859-1");
			Transport.send(message);
		} catch (MessagingException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
