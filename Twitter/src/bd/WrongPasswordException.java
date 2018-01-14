package bd;

public class WrongPasswordException extends Exception {
	public WrongPasswordException(String message) {
		System.out.println(message);
	}
}
