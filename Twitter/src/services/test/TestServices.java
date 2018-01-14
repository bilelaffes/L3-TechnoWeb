package services.test;

import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;

import bd.CommentsTools;
import bd.FriendsTools;
import services.UserServices;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class TestServices {

	public static void main(String[] args) {
		// Database.createTableFriendsRequest();
		/*
		 * Date date = new Date();
		 * CommentsTools.insertComment("008348ee-80dc-4236-b23b-56e0e421fcb3",
		 * date, "Vous etes revissante"); JSONObject json =
		 * CommentsTools.getCommentById(5); System.out.println(json.toString());
		 * System.out.println(
		 * "***********************************************************");
		 * CommentsTools.removeComment("008348ee-80dc-4236-b23b-56e0e421fcb3",
		 * "Vous etes revissante"); json = CommentsTools.getCommentById(2);
		 * System.out.println(json.toString());
		 * System.out.println(UserServices.listFriend(3));
		 */
		// UserServices.addComment("da89de3d-0f95-48f4-9277-2a92108a5a9b","lol");
		// UserServices.removeComment(36,"bouji","@admin2
		// helllooooooooooooooo\n");
		// System.out.println(UserServices.getID("cyril"));
		// JSONObject json = CommentsTools.getCommentById(18);
		// System.out.println(json);
		/*
		 * try { boolean bool = FriendsTools.friendRequest(18, 21);
		 * System.out.println(bool); } catch (InstantiationException |
		 * IllegalAccessException | ClassNotFoundException | SQLException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		/*
		 * String[] tab ={"bilel","Faucon"}; JSONObject json =
		 * UserServices.insertCommentList(tab,"aaaaaaa");
		 * System.out.println(json);
		 */

		/*
		 * JSONObject json2 = UserServices.listFriend(18);
		 * System.out.println(json2);
		 */
		/*
		 * JSONObject json = UserServices.getCommentList();
		 * System.out.println(json);
		 */
		/*
		 * JSONObject json = UserServices.getNomPrenomExist("B");
		 * System.out.println(json);
		 */
		/*
		 * JSONObject json =
		 * UserServices.logout("a92cc0a8-1076-4afc-bcbd-64cf5a9190a6");
		 * System.out.println(json);
		 */
		UserServices.sendMessage("TEST","motdepasse21","bilelaffes@hotmail.com");

	}

}
