package main.exception;

public class CardCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6815762110913723980L;
	
	private String message;
	
	public CardCreationException(String msg) {
		message = msg;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
