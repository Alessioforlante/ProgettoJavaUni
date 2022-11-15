package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.UtenteNotFoundExc;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Utente;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class StartupController implements Initializable, DataInitializable<Utente> {

	private ViewDispatcher dispatcher;
	private UtenteService utenteService;

	@FXML
	private Label loginErrorLabel;

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Button loginButton;

	public StartupController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginButton.disableProperty().bind(password.textProperty().isEmpty().or(username.textProperty().isEmpty()));
	}

	@FXML
	private void loginAction(ActionEvent event) {
		try {
			Utente utente = utenteService.authenticate(username.getText(), password.getText());
			dispatcher.loggedIn(utente);
		} catch (UtenteNotFoundExc e) {
			loginErrorLabel.setText("Utente non trovato!");
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

	/*
	 * invocato se l'utilizzatore è un utente passo alla schermata di
	 * autenticazione/registrazione degli utenti
	 */

	@FXML
	private void userButtonAction(ActionEvent event) {
		dispatcher.userRegView();
	}

	/*
	 * invocato se l'utilizzatore è un'azienda passo alla schermata di
	 * autenticazione/registrazione delle aziende
	 */

	@FXML
	private void companyButtonAction(ActionEvent event) {
		dispatcher.companyRegView();
	}

}
