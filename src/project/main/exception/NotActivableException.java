package project.main.exception;

public class NotActivableException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6594287163508294372L;
	
	private String message;
	
	public NotActivableException(String msg) {
		message = msg;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
