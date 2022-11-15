package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.Utente;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class LayoutController implements Initializable, DataInitializable<Utente> {

	private static final MenuElement[] ELEMENTI_AZIENDA = { new MenuElement("Inserisci offerta di lavoro", "offer"),
			new MenuElement("Visualizza le offerte inserite", "inserted_offers"),
			new MenuElement("Candidature ricevute", "applications")

	};

	private static final MenuElement[] ELEMENTI_LAVORATORE = { new MenuElement("Ricerca offerte di lavoro", "offers"),
			new MenuElement("Offerte di lavoro attinenti", "best_offers"),
			new MenuElement("Candidature inviate", "applications_sent"),
			new MenuElement("Visualizza messaggi", "messages") };

	private Button logoutButton = new Button("Disconnettiti");

	private ViewDispatcher dispatcher;
	private Utente utente;

	@FXML
	private HBox buttonsHBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dispatcher.logout();
			}
		});
		logoutButton.setStyle("-fx-font-size: 14; margin-left: 100px;");
		logoutButton.setPrefHeight(10);
		logoutButton.setPrefWidth(160);
		dispatcher = ViewDispatcher.getInstance();
	}

	/*
	 * Ovverriding del metodo initializeData dell'interfaccia DataInitializable
	 * Questo permette di gestire il contenuto da visualizzare sulla base del ruolo
	 * dell'utilizzatore dell'applicazione
	 */

	@Override
	public void initializeData(Utente utente) {
		this.utente = utente;

		// Se l'utente tenta di loggarsi come azienda
		if (utente instanceof Azienda) {
			for (MenuElement elemento : ELEMENTI_AZIENDA) {
				buttonsHBox.getChildren().add(createButton(elemento));
			}
			buttonsHBox.getChildren().add(logoutButton);
		}

		// Se l'utente tenta di loggarsi come lavoratore
		if (utente instanceof Lavoratore) {
			for (MenuElement elemento : ELEMENTI_LAVORATORE) {
				buttonsHBox.getChildren().add(createButton(elemento));
			}
			buttonsHBox.getChildren().add(logoutButton);
		}

	}

	@FXML
	public void logoutButtonAction() {
		dispatcher.logout();
	}

	private Button createButton(MenuElement viewItem) {
		Button button = new Button(viewItem.getTesto());
		button.setStyle("-fx-font-size: 14; margin-left: 100px;");
		button.setPrefHeight(10);
		button.setPrefWidth(180);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dispatcher.renderView(viewItem.getVista(), utente);
			}
		});
		return button;
	}

}