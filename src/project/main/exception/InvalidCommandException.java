package project.main.exception;

public class InvalidCommandException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 214098354946673698L;
	private String message;
	
	public InvalidCommandException(String command, String additionalInfo) {
		message = command + " is not a valid command! ("+additionalInfo+")";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
