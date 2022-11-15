package it.univaq.disim.oop.croissantmanager.business;

// Eccezione sollevata quando l'utente (azienda o lavoratore) non viene trovato

public class UtenteNotFoundExc extends BusinessExc {

	public UtenteNotFoundExc() {
		super();
	}

	public UtenteNotFoundExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UtenteNotFoundExc(String message, Throwable cause) {
		super(message, cause);
	}

	public UtenteNotFoundExc(String message) {
		super(message);
	}

	public UtenteNotFoundExc(Throwable cause) {
		super(cause);
	}

}
