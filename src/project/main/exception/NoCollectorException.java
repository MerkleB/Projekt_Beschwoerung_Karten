package project.main.exception;

public class NoCollectorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2201349519299861015L;
	
	private String message;
	
	public NoCollectorException(String msg) {
		message = msg;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	
}
