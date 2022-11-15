package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.Utente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class WelcomeController implements Initializable, DataInitializable<Utente> {

	@FXML
	private Label welcomeLabel;
	@FXML
	private Label welcomeLabelS;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void initializeData(Utente utente) {
		if (utente instanceof Azienda) {
			welcomeLabel.setText("Benvenuto " + ((Azienda) utente).getRagione());
			welcomeLabelS.setText("Benvenuto " + ((Azienda) utente).getRagione());
		} else {
			welcomeLabel
					.setText("Benvenuto " + ((Lavoratore) utente).getNome() + " " + ((Lavoratore) utente).getCognome());
			welcomeLabelS
					.setText("Benvenuto " + ((Lavoratore) utente).getNome() + " " + ((Lavoratore) utente).getCognome());

		}
	}

}
