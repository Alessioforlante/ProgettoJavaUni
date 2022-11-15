package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.NoOffersFoundExc;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.domain.Candidatura;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ApplicationsSentController implements Initializable, DataInitializable<Lavoratore> {

	private ViewDispatcher dispatcher;
	private Lavoratore lavoratore;
	private List<Candidatura> candidature;
	private OffertaService offertaService;

	@FXML
	private TableView<Candidatura> applicationsSentTable;

	@FXML
	private TableColumn<Candidatura, String> aziendaColumn;

	@FXML
	private TableColumn<Candidatura, String> dataColumn;

	@FXML
	private TableColumn<Candidatura, ButtonBar> azioniColumn;

	@FXML
	private Label errorLabel;

	public ApplicationsSentController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		offertaService = factory.getOffertaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/* Inizializzo i valori nella tabella. */

		aziendaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Candidatura, String> param) {
						return new SimpleObjectProperty<String>(
								param.getValue().getOfferta().getAzienda().getRagione());
					}
				});
		dataColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Candidatura, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getDataCandidatura().toString());
					}
				});
		azioniColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, ButtonBar>, ObservableValue<ButtonBar>>() {
					@Override
					public ObservableValue<ButtonBar> call(CellDataFeatures<Candidatura, ButtonBar> param) {
						final ButtonBar azioniButtonBar = new ButtonBar();
						final Button eliminaButton = new Button();

						eliminaButton.setText("Elimina");
						eliminaButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									offertaService.removeCandidatura(param.getValue());
									initializeData(lavoratore);
									errorLabel.setText("Candidatura eliminata!");
								} catch (BusinessExc e) {
									dispatcher.renderError(e);
								}
							}
						});
						azioniButtonBar.getButtons().addAll(eliminaButton);
						return new SimpleObjectProperty<ButtonBar>(azioniButtonBar);
					}
				});
	}

	@Override
	public void initializeData(Lavoratore lavoratore) {
		this.lavoratore = lavoratore;
		try {
			this.candidature = offertaService.findCandidatureByLavoratore(this.lavoratore);
			ObservableList<Candidatura> offerteData = FXCollections.observableArrayList(this.candidature);
			applicationsSentTable.setItems(offerteData);
		} catch (NoOffersFoundExc e) {
			errorLabel.setText("Nessuna offerta trovata!");
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

}
