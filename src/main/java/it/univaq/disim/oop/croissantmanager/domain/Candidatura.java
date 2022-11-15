package it.univaq.disim.oop.croissantmanager.domain;

import java.time.LocalDate;

public class Candidatura {
	private Integer id;
	private LocalDate dataCandidatura;
	private Lavoratore lavoratore;
	private OffertaLavoro offerta;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDataCandidatura() {
		return dataCandidatura;
	}

	public void setDataCandidatura(LocalDate dataCandidatura) {
		this.dataCandidatura = dataCandidatura;
	}

	public Lavoratore getLavoratore() {
		return lavoratore;
	}

	public void setLavoratore(Lavoratore lavoratore) {
		this.lavoratore = lavoratore;
	}

	public OffertaLavoro getOfferta() {
		return offerta;
	}

	public void setOfferta(OffertaLavoro offerta) {
		this.offerta = offerta;
	}

}