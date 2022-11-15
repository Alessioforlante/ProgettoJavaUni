package it.univaq.disim.oop.croissantmanager.business;

// Eccezione sollevata quando non vi sono offerte per la ricerca effettuata

public class NoOffersFoundExc extends BusinessExc {

	public NoOffersFoundExc() {
		super();
	}

	public NoOffersFoundExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoOffersFoundExc(String message, Throwable cause) {
		super(message, cause);
	}

	public NoOffersFoundExc(String message) {
		super(message);
	}

	public NoOffersFoundExc(Throwable cause) {
		super(cause);
	}

}
