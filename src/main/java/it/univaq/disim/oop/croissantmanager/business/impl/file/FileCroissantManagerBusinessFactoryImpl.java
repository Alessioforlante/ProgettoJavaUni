package it.univaq.disim.oop.croissantmanager.business.impl.file;

import java.io.File;

import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;

/* Sottoclasse della Abstract Factory: vengono create implementazione dei service che lavorano su File */

public class FileCroissantManagerBusinessFactoryImpl extends CroissantManagerBusinessFactory {

	private static final String REPOSITORY_BASE = "src" + File.separator + "main" + File.separator + "resources"
			+ File.separator + "data";
	private static final String UTENTI_FILE_NAME = REPOSITORY_BASE + File.separator + "utenti.txt";
	private static final String MESSAGGI_FILE_NAME = REPOSITORY_BASE + File.separator + "messaggi.txt";
	private static final String OFFERTE_FILE_NAME = REPOSITORY_BASE + File.separator + "offerte.txt";
	private static final String CANDIDATURE_FILE_NAME = REPOSITORY_BASE + File.separator + "candidature.txt";

	private UtenteService utenteService;
	private OffertaService offertaService;

	public FileCroissantManagerBusinessFactoryImpl() {
		utenteService = new FileUtenteServiceImpl(UTENTI_FILE_NAME, MESSAGGI_FILE_NAME);
		offertaService = new FileOffertaServiceImpl(REPOSITORY_BASE, OFFERTE_FILE_NAME, CANDIDATURE_FILE_NAME,
				utenteService);
	}

	@Override
	public UtenteService getUtenteService() {
		return utenteService;
	}

	@Override
	public OffertaService getOffertaService() {
		return offertaService;
	}

}
