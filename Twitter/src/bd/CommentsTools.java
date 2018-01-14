package bd;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import services.ServicesTools;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 *
 */
public class CommentsTools {
	/**
	 * 
	 * @param id
	 * @param nom
	 * @param date
	 * @param commentaire
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean insertComment(int id,Date date, String commentaire) {
		try {
			if (!UserTools.idExists(id)) {
				return false;
			}
			Mongo m = new Mongo(DBStatic.mongoDB_host, DBStatic.mongoDB_port);
			DB db = m.getDB(DBStatic.mysql_db);
			DBCollection collection = db.getCollection("comments");
			BasicDBObject object = new BasicDBObject();
			String prenom = UserTools.getPrenom(id);
			String login = UserTools.getLogin(prenom);
			object.put("id", id);
			object.put("_auteur", prenom);
			object.put("_date", date);
			object.put("_comment", commentaire);
			object.put("_login", login);
			collection.insert(object);
			m.close();
		} catch (UnknownHostException | MongoException | InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static JSONObject insertCommentaire(int id,int id_log,Date date, String commentaire) {
		try {
			if (!UserTools.idExists(id_log)) {
				return ServicesTools.serviceRefused("idExistePas",9);
			}
			Mongo m = new Mongo(DBStatic.mongoDB_host, DBStatic.mongoDB_port);
			DB db = m.getDB(DBStatic.mysql_db);
			DBCollection collection = db.getCollection("comments");
			BasicDBObject object = new BasicDBObject();
			String prenom = UserTools.getPrenom(id);
			String login = UserTools.getLogin(prenom);
			object.put("id", id_log);
			object.put("_auteur", prenom);
			object.put("_date", date);
			object.put("_comment", commentaire);
			object.put("_login", login);
			collection.insert(object);
			m.close();
		} catch (UnknownHostException | MongoException | InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return ServicesTools.serviceAccepted();
	}
	/**
	 * 
	 * @param id
	 * @param nom
	 * @param commentaire
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean removeComment(int id,String login, String commentaire) {
		try {
			if (!UserTools.idExists(id)) {
				return false;
			}
			Mongo m = new Mongo(DBStatic.mongoDB_host, DBStatic.mongoDB_port);
			DB db = m.getDB(DBStatic.mysql_db);
			DBCollection collection = db.getCollection("comments");
			BasicDBObject object = new BasicDBObject();
			int id_log = UserTools.getIdUser(login);
			String prenom = UserTools.getPrenom(id_log);
			object.put("id", id);
			object.put("_auteur", prenom);
			object.put("_comment", commentaire);
			DBCursor cursor = collection.find(object);
			while (cursor.hasNext()) {
				DBObject res = cursor.next();
				collection.remove(res);
			}
			cursor.close();
			m.close();
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| UnknownHostException | MongoException e) {
			e.getMessage();
		}
		return true;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static JSONObject getCommentById(int id) {
		JSONObject json = null;
		try {
			if (!UserTools.idExists(id)) {
				return ServicesTools.serviceRefused("ErreurID", -1);
			}
			Mongo m = new Mongo(DBStatic.mongoDB_host, DBStatic.mongoDB_port);
			DB db = m.getDB(DBStatic.mysql_db);
			DBCollection collection = db.getCollection("comments");
			BasicDBObject object = new BasicDBObject();
			json = new JSONObject();
			object.put("id", id);
			DBCursor cursor = collection.find(object);
			while (cursor.hasNext()) {
				DBObject res = cursor.next();
				json.accumulate("tweets", res);
			}
			cursor.close();
			m.close();
		} catch (JSONException | InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException
				| UnknownHostException | MongoException e) {
			e.getMessage();
		}
		return json;
	}
	
	public static JSONObject getComment(String comment, int id, String amiOuTout) {
		JSONObject json = null;
		try {
			if (comment == null || amiOuTout == null) {
				return ServicesTools.serviceRefused("PbArgument", -1);
			}
			if (amiOuTout.equals("")) {
				return ServicesTools.serviceRefused("VousDevezCocherLuneDesCheckBox", 20);
			}
			Mongo m = new Mongo(DBStatic.mongoDB_host, DBStatic.mongoDB_port);
			DB db = m.getDB(DBStatic.mysql_db);
			DBCollection collection = db.getCollection("comments");
			BasicDBObject object = new BasicDBObject();
			json = new JSONObject();
			ArrayList<String> listAuteur = FriendsTools.listFriend2(id);
			object.put("_comment", Pattern.compile(".*" + comment));
			DBCursor cursor = collection.find(object);
			while (cursor.hasNext()) {
				DBObject res = cursor.next();
				if (amiOuTout.equals("Amis")) {
					for (String auteur : listAuteur) {
						if (res.get("_auteur").equals(auteur)) {
							json.accumulate("tweets", res);
						}
					}
				}else{
					json.accumulate("tweets", res);
				}
			}
			cursor.close();
			m.close();
		} catch (JSONException | UnknownHostException | MongoException | InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.getMessage();
		}
		return json;
	}
	public static JSONObject getCommentAll() {
		JSONObject json = null;
		try {

			Mongo m = new Mongo(DBStatic.mongoDB_host, DBStatic.mongoDB_port);
			DB db = m.getDB(DBStatic.mysql_db);
			DBCollection collection = db.getCollection("comments");
			BasicDBObject object = new BasicDBObject();
			json = new JSONObject();
			object.put("_comment", Pattern.compile(".*"));
			DBCursor cursor = collection.find(object);
			while (cursor.hasNext()) {
				DBObject res = cursor.next();
				json.accumulate("tweets", res);
			}
			cursor.close();
			m.close();
		} catch (JSONException | UnknownHostException | MongoException e) {
			e.getMessage();
		}
		return json;
	}
}