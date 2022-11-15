package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.ExistingUsernameExc;
import it.univaq.disim.oop.croissantmanager.business.PasswordsNotMatchingExc;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterCompanyController implements Initializable, DataInitializable<Azienda> {

	private ViewDispatcher dispatcher;
	private UtenteService utenteService;

	@FXML
	private TextField usernameTextField;

	@FXML
	private TextField ragioneTextField;

	@FXML
	private TextField ambitoTextField;

	@FXML
	private TextField sedeTextField;

	@FXML
	private TextField dipendentiTextField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private PasswordField repeatPasswordField;

	@FXML
	private Label errorLabel;

	@FXML
	private Button registerButton;

	@FXML
	private Hyperlink backButton;

	public RegisterCompanyController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		registerButton.disableProperty()
				.bind(usernameTextField.textProperty().isEmpty()
						.or(ragioneTextField.textProperty().isEmpty().or(ambitoTextField.textProperty().isEmpty()
								.or(sedeTextField.textProperty().isEmpty()
										.or(dipendentiTextField.textProperty().isEmpty().or(passwordField.textProperty()
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
			 * Lancia un'eccezione se l'username scelto è già esistente oppure se le
			 * password inserite non coincidono
			 */

			if (!(passwordField.getText().equals(repeatPasswordField.getText()))) {
				throw new PasswordsNotMatchingExc();
			}
			if (utenteService.isAlreadyExisting(usernameTextField.getText())) {
				throw new ExistingUsernameExc();
			}
			Azienda azienda = new Azienda();
			azienda.setUsername(usernameTextField.getText());
			azienda.setPassword(passwordField.getText());
			azienda.setRagione(ragioneTextField.getText());
			azienda.setAmbito(ambitoTextField.getText());
			azienda.setSede(sedeTextField.getText());
			azienda.setNumeroDipendenti(Integer.parseInt(dipendentiTextField.getText()));
			utenteService.registerAsCompany(azienda);
			dispatcher.logout();
		} catch (IllegalArgumentException e) {
			errorLabel.setText("Per favore, inserisci un numero nel campo 'N. Dipendenti'");
		} catch (PasswordsNotMatchingExc e) {
			errorLabel.setText("Le password inserite non coincidono!");
		} catch (ExistingUsernameExc e) {
			errorLabel.setText("Lo username scelto è già esistente!");
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

}
