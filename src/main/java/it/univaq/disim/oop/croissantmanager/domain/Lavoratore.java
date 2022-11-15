package it.univaq.disim.oop.croissantmanager.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lavoratore extends Utente {
	private String nome;
	private String cognome;
	private String luogoNascita;
	private LocalDate dataNascita;
	private Set<Competenza> competenzePossedute;
	private List<Candidatura> candidature = new ArrayList<>();
	private Set<Messaggio> messaggiRicevuti = new HashSet<>();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public Set<Competenza> getCompetenzePossedute() {
		return competenzePossedute;
	}

	public void setCompetenzePossedute(Set<Competenza> competenzePossedute) {
		this.competenzePossedute = competenzePossedute;
	}

	public List<Candidatura> getCandidature() {
		return candidature;
	}

	public void setCandidature(List<Candidatura> candidature) {
		this.candidature = candidature;
	}

	public Set<Messaggio> getMessaggi() {
		return messaggiRicevuti;
	}

	public void setMessaggi(Set<Messaggio> messaggiRicevuti) {
		this.messaggiRicevuti = messaggiRicevuti;
	}

	public void addMessaggio(Messaggio messaggio) {
		this.messaggiRicevuti.add(messaggio);
	}

}
