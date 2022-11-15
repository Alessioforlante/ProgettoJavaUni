package it.univaq.disim.oop.croissantmanager.business;

// Eccezione sollevata quando si verifica un generico evento anomalo relativo alla logica di business

public class BusinessExc extends Exception {

	public BusinessExc() {
		super();
	}

	public BusinessExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BusinessExc(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessExc(String message) {
		super(message);
	}

	public BusinessExc(Throwable cause) {
		super(cause);
	}

}
