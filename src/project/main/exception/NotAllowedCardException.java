package project.main.exception;

public class NotAllowedCardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7766963672157067405L;
	
	private String message;
	
	public NotAllowedCardException(String msg) {
		message = msg;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
