package it.univaq.disim.oop.croissantmanager.domain;

public class Messaggio {
	private Azienda azienda;
	private Lavoratore lavoratore;
	private String testo;

	public Azienda getAzienda() {
		return azienda;
	}

	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}

	public Lavoratore getLavoratore() {
		return lavoratore;
	}

	public void setLavoratore(Lavoratore lavoratore) {
		this.lavoratore = lavoratore;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
}