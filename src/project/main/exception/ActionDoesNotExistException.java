package project.main.exception;

public class ActionDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -367865085473455646L;
	
	private String message;
	
	public ActionDoesNotExistException(String action_name) {
		message = action_name + " does not exist";
	}

	@Override
	public String getMessage() {
		return message;
	}


}
