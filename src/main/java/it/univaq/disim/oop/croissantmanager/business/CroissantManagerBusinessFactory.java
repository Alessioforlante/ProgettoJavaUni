package it.univaq.disim.oop.croissantmanager.business;

import it.univaq.disim.oop.croissantmanager.business.impl.ram.RAMCroissantManagerBusinessFactoryImpl;

/* Utilizziamo una Abstract Factory */

public abstract class CroissantManagerBusinessFactory {

	private static CroissantManagerBusinessFactory factory = new RAMCroissantManagerBusinessFactoryImpl();
//	private static CroissantManagerBusinessFactory factory = new FileCroissantManagerBusinessFactoryImpl();

	public static CroissantManagerBusinessFactory getInstance() {
		return factory;
	}

	public abstract UtenteService getUtenteService();

	public abstract OffertaService getOffertaService();

}
