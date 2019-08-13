package main.exception;

public class InvalidCardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6611420140190291407L;
	
	private String message;
	
	public InvalidCardException(String msg) {
		message = msg;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
