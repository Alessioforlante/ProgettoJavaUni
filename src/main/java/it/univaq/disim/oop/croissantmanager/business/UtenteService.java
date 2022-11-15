package it.univaq.disim.oop.croissantmanager.business;

import java.util.Set;

import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.Messaggio;
import it.univaq.disim.oop.croissantmanager.domain.Utente;

// 'Utente' indica sia i Lavoratori, sia le Aziende

public interface UtenteService {

	/* Metodi relativi agli utenti */

	Utente authenticate(String username, String password) throws UtenteNotFoundExc, BusinessExc;

	void registerAsWorker(Utente lavoratore) throws BusinessExc;

	void registerAsCompany(Utente azienda) throws BusinessExc;

	boolean isAlreadyExisting(String username) throws BusinessExc;

	/* Metodi relativi ai messaggi */

	void sendMessaggio(Messaggio messaggio, Lavoratore lavoratore) throws BusinessExc;

	Set<StringBuilder> findAllMessaggi(Lavoratore lavoratore) throws BusinessExc;

}
