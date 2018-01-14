package services;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */

public class ServicesTools {

	/**
	 * 
	 * @param message
	 * @param codeErreur
	 * @return json
	 */
	public static JSONObject serviceRefused(String message, int codeErreur) {

		JSONObject jason = new JSONObject();
		try {
			jason.put(message, codeErreur);
		} catch (JSONException e) {
			e.getMessage();
		}
		return jason;
	}

	/**
	 * 
	 * @return json
	 */

	public static JSONObject serviceAccepted() {

		JSONObject jason = new JSONObject();
		try {
			jason.put("OK", 200);
		} catch (JSONException e) {
			e.getMessage();
		}
		return jason;
	}
}
