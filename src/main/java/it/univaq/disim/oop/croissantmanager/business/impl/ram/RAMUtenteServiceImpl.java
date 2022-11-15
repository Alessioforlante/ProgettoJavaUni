package it.univaq.disim.oop.croissantmanager.business.impl.ram;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.UtenteNotFoundExc;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.Messaggio;
import it.univaq.disim.oop.croissantmanager.domain.Utente;

public class RAMUtenteServiceImpl implements UtenteService {

	private static Set<Utente> utentiRegistrati = new HashSet<>();
	private static int lastId = 1;

	public RAMUtenteServiceImpl() {
	}

	/* Metodi relativi agli utenti */

	@Override
	public Utente authenticate(String username, String password) throws BusinessExc {
		for (Utente utente : utentiRegistrati) {
			if (utente.getUsername().equals(username) && utente.getPassword().equals(password)) {
				return utente;
			}
		}

		/*
		 * Lancio l'eccezione UtenteNotFoundExc se l'utente che tenta di autenticarsi
		 * non è stato trovato
		 */

		throw new UtenteNotFoundExc();
	}

	@Override
	public void registerAsWorker(Utente lavoratore) throws BusinessExc {
		lavoratore.setId(lastId++);
		utentiRegistrati.add(lavoratore);
	}

	@Override
	public void registerAsCompany(Utente azienda) throws BusinessExc {
		azienda.setId(lastId++);
		utentiRegistrati.add(azienda);
	}

	/*
	 * Il metodo isAlreadyExisting controlla l'esistenza di un utente (azienda o
	 * lavoratore) con un username corrispondente alla stringa passata come
	 * parametro
	 */
	@Override
	public boolean isAlreadyExisting(String username) throws BusinessExc {
		for (Utente utente : utentiRegistrati) {
			if (utente.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	/* Metodi relativi ai messaggi */

	@Override
	public void sendMessaggio(Messaggio messaggio, Lavoratore lavoratore) throws BusinessExc {
		lavoratore.addMessaggio(messaggio);
	}

	@Override
	public Set<StringBuilder> findAllMessaggi(Lavoratore lavoratore) throws BusinessExc {
		Set<StringBuilder> result = new LinkedHashSet<>();
		for (Messaggio messaggio : lavoratore.getMessaggi()) {
			StringBuilder message = new StringBuilder();
			message.append(messaggio.getTesto());
			result.add(message);
		}
		return result;
	}

}
