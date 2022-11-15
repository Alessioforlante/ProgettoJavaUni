package it.univaq.disim.oop.croissantmanager.domain;

import java.util.HashSet;
import java.util.Set;

public class Azienda extends Utente {
	private String ragione;
	private String ambito;
	private String sede;
	private Set<OffertaLavoro> offerte = new HashSet<>();
	private int numeroDipendenti;
	private Set<Messaggio> messaggiInviati = new HashSet<>();

	public String getRagione() {
		return ragione;
	}

	public void setRagione(String ragione) {
		this.ragione = ragione;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public Set<OffertaLavoro> getOfferte() {
		return offerte;
	}

	public void setOfferte(Set<OffertaLavoro> offerte) {
		this.offerte = offerte;
	}

	public int getNumeroDipendenti() {
		return numeroDipendenti;
	}

	public void setNumeroDipendenti(int numeroDipendenti) {
		this.numeroDipendenti = numeroDipendenti;
	}

	public Set<Messaggio> getMessaggiInviati() {
		return messaggiInviati;
	}

	public void setMessaggiInviati(Set<Messaggio> messaggiInviati) {
		this.messaggiInviati = messaggiInviati;
	}

}
