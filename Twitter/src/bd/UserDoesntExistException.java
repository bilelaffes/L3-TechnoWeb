package bd;

/**
 * 
 * @author Benchadi Yousria
 * @author Affes Bilel
 */
public class UserDoesntExistException extends Exception {
	public UserDoesntExistException(String message) {
		System.out.println(message);
	}
}
