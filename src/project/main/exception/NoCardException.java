package project.main.exception;

public class NoCardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1843127944722158567L;
	
	private String message;
	
	public NoCardException(String msg) {
		message = msg;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
