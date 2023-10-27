package excepciones;

public class AeropuertosException extends Exception {

	private static final long serialVersionUID = 9075095558068192043L;

	public AeropuertosException() {
		super();
	}

	public AeropuertosException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AeropuertosException(String message, Throwable cause) {
		super(message, cause);
	}

	public AeropuertosException(String message) {
		super(message);
	}

	public AeropuertosException(Throwable cause) {
		super(cause);
	}
	
	
}
