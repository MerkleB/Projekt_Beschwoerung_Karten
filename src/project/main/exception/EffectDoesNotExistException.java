package project.main.exception;

public class EffectDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9110581420560262779L;
	
	private String message;
	
	public EffectDoesNotExistException(String effect_name) {
		message = effect_name + " does not exist";
	}

	@Override
	public String getMessage() {
		return message;
	}

}
