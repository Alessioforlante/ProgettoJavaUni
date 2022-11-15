package it.univaq.disim.oop.croissantmanager.business.impl.ram;

import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;

/* Sottoclasse della Abstract Factory: vengono create implementazione dei service che lavorano in RAM */

public class RAMCroissantManagerBusinessFactoryImpl extends CroissantManagerBusinessFactory {

	private UtenteService utenteService;
	private OffertaService offertaService;

	public RAMCroissantManagerBusinessFactoryImpl() {
		utenteService = new RAMUtenteServiceImpl();
		offertaService = new RAMOffertaServiceImpl();
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
