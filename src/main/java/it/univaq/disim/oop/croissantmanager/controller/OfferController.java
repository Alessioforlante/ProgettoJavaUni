package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Competenza;
import it.univaq.disim.oop.croissantmanager.domain.OffertaLavoro;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class OfferController implements Initializable, DataInitializable<Azienda> {

	private Azienda azienda;
	private ViewDispatcher dispatcher;
	private OffertaService offertaService;

	@FXML
	private TextField localitaTextField;

	@FXML
	private TextField settoreTextField;

	@FXML
	private TextField salarioTextField;

	@FXML
	private Button insertButton;

	@FXML
	private Label errorLabel;

	@FXML
	private Label errorShadowLabel;

	@FXML
	private ListView<CheckBox> competenzeListView;

	public OfferController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		offertaService = factory.getOffertaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		 * Popolo la listview con una checkbox per ognuna delle competenze ammesse dal
		 * sistema
		 */

		for (Competenza competenza : Competenza.values()) {
			CheckBox checkbox = new CheckBox();
			checkbox.setText(competenza.toString());
			competenzeListView.getItems().add(checkbox);
		}
		insertButton.disableProperty().bind(localitaTextField.textProperty().isEmpty()
				.or(settoreTextField.textProperty().isEmpty().or(salarioTextField.textProperty().isEmpty())));
	}

	@Override
	public void initializeData(Azienda azienda) {
		this.azienda = azienda;
	}

	public void insertButtonAction(ActionEvent event) {
		Set<Competenza> competenzeRichieste = new HashSet<>();
		try {

			/*
			 * Per ogni checkbox spuntata, aggiungo la relativa competenza alla collection
			 * appena creata.
			 */

			for (CheckBox checkbox : competenzeListView.getItems()) {
				if (checkbox.isSelected()) {
					competenzeRichieste.add(Competenza.getCompetenzaByText(checkbox.getText()));
				}
			}
			OffertaLavoro offerta = new OffertaLavoro();
			offerta.setAzienda(azienda);
			offerta.setSettore(settoreTextField.getText());
			offerta.setSalario(Float.parseFloat(salarioTextField.getText()));
			offerta.setDataInserimento(LocalDate.now());
			offerta.setLocalita(localitaTextField.getText());
			offerta.setCompetenzeRichieste(competenzeRichieste);
			azienda.getOfferte().add(offerta);
			offertaService.addOfferta(offerta);
			errorLabel.setText("Offerta inserita!");
			errorShadowLabel.setText("Offerta inserita!");

			/* Svuoto i tre TextField */

			localitaTextField.setText("");
			salarioTextField.setText("");
			settoreTextField.setText("");
		} catch (IllegalArgumentException e) {
			errorLabel.setText("Per favore, inserisci un numero nel campo 'Salario'");
			errorShadowLabel.setText("Per favore, inserisci un numero nel campo 'Salario'");
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

}
