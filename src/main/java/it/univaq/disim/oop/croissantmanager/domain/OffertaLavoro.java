package it.univaq.disim.oop.croissantmanager.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OffertaLavoro {
	private Integer id;
	private String localita;
	private LocalDate dataInserimento;
	private Azienda azienda;
	private String settore;
	private String salario;
	private Set<Competenza> competenzeRichieste;
	private List<Candidatura> candidature = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public LocalDate getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(LocalDate dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Azienda getAzienda() {
		return azienda;
	}

	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}

	public String getSettore() {
		return settore;
	}

	public void setSettore(String settore) {
		this.settore = settore;
	}

	public String getSalario() {
		return salario;
	}

	public void setSalario(float salario) {
		this.salario = String.valueOf(salario);
	}

	public Set<Competenza> getCompetenzeRichieste() {
		return competenzeRichieste;
	}

	public void setCompetenzeRichieste(Set<Competenza> competenzeRichieste) {
		this.competenzeRichieste = competenzeRichieste;
	}
	
	public List<Candidatura> getCandidature() {
		return candidature;
	}

	public void setCandidature(List<Candidatura> candidature) {
		this.candidature = candidature;
	}


}
