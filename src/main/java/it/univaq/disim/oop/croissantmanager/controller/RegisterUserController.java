package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.ExistingUsernameExc;
import it.univaq.disim.oop.croissantmanager.business.PasswordsNotMatchingExc;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.Utente;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterUserController implements Initializable, DataInitializable<Utente> {

	private ViewDispatcher dispatcher;
	private UtenteService utenteService;

	@FXML
	private Label errorLabel;

	@FXML
	private TextField usernameTextField;

	@FXML
	private TextField nomeTextField;

	@FXML
	private TextField cognomeTextField;

	@FXML
	private DatePicker dataPicker;

	@FXML
	private TextField luogoNascitaTextField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private PasswordField repeatPasswordField;

	@FXML
	private Hyperlink backButton;

	@FXML
	private Button registerButton;

	public RegisterUserController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		registerButton.disableProperty()
				.bind(nomeTextField.textProperty().isEmpty()
						.or(cognomeTextField.textProperty().isEmpty().or(nomeTextField.textProperty().isEmpty()
								.or(usernameTextField.textProperty().isEmpty().or(
										luogoNascitaTextField.textProperty().isEmpty().or(passwordField.textProperty()
												.isEmpty().or(repeatPasswordField.textProperty().isEmpty())))))));
	}

	@FXML
	private void backButtonAction() {
		dispatcher.logout();
	}

	@FXML
	public void registerButtonAction(ActionEvent event) {
		try {

			/*
			 * Lancia un'eccezione se lo username scelto è già esistente, se le password non
			 * coincidono oppure se non è stata inserita una data
			 */

			if (!(passwordField.getText().equals(repeatPasswordField.getText()))) {
				throw new PasswordsNotMatchingExc();
			}
			if (utenteService.isAlreadyExisting(usernameTextField.getText())) {
				throw new ExistingUsernameExc();
			}
			if (dataPicker.getValue() == null) {
				throw new IllegalArgumentException();
			}
			Lavoratore lavoratore = new Lavoratore();
			lavoratore.setUsername(usernameTextField.getText());
			lavoratore.setPassword(passwordField.getText());
			lavoratore.setNome(nomeTextField.getText());
			lavoratore.setCognome(cognomeTextField.getText());
			lavoratore.setLuogoNascita(luogoNascitaTextField.getText());
			lavoratore.setDataNascita(dataPicker.getValue());
			dispatcher.userSkillsView(lavoratore);
		} catch (IllegalArgumentException e) {
			errorLabel.setText("Per favore, inserisci una data valida!");
		} catch (PasswordsNotMatchingExc e) {
			errorLabel.setText("Le password inserite non coincidono!");
		} catch (ExistingUsernameExc e) {
			errorLabel.setText("Lo username scelto è già esistente!");
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}
}
