package it.univaq.disim.oop.croissantmanager.controller;

public class MenuElement {

	private String testo;
	private String vista;

	public MenuElement(String testo, String vista) {
		this.testo = testo;
		this.vista = vista;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}
}
