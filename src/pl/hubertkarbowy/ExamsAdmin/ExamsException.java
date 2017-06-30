package pl.hubertkarbowy.ExamsAdmin;

/**
 * Thrown if something goes wrong with the application
 * The class ExamsException extends {@link RuntimeException} and is thrown each time the application encounters an error
 * parsing the client-server commands and responses.
 * 
 * This exception is caught "globally" in {@link ExamsAdmin} where a dialog is displayed. If a message is provided, it will be shown in the dialog window.
 *
 */
class ExamsException extends RuntimeException {

	public ExamsException() {
		super();
	}

	public ExamsException(String message) {
		super(message);
	}
}
