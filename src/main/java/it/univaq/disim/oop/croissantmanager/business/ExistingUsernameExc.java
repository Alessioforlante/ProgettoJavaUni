package it.univaq.disim.oop.croissantmanager.business;

/* Eccezione sollevata quando l'utente sceglie in fase di registrazione uno username già utilizzato */

public class ExistingUsernameExc extends BusinessExc {

	public ExistingUsernameExc() {
		super();
	}

	public ExistingUsernameExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistingUsernameExc(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistingUsernameExc(String message) {
		super(message);
	}

	public ExistingUsernameExc(Throwable cause) {
		super(cause);
	}

}
