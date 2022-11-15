package it.univaq.disim.oop.croissantmanager.business;

// Eccezione sollevata quando le password inserite non coincidono

public class PasswordsNotMatchingExc extends BusinessExc {

	public PasswordsNotMatchingExc() {
		super();
	}

	public PasswordsNotMatchingExc(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PasswordsNotMatchingExc(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordsNotMatchingExc(String message) {
		super(message);
	}

	public PasswordsNotMatchingExc(Throwable cause) {
		super(cause);
	}

}
