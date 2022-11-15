package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.OffertaLavoro;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class InsertedOffersController implements Initializable, DataInitializable<Azienda> {

	private Azienda azienda;
	private Set<OffertaLavoro> offerte;
	private ViewDispatcher dispatcher;
	private OffertaService offertaService;

	@FXML
	private TableView<OffertaLavoro> insertedOffersTable;

	@FXML
	private TableColumn<OffertaLavoro, String> settoreColumn;

	@FXML
	private TableColumn<OffertaLavoro, String> salarioColumn;

	@FXML
	private TableColumn<OffertaLavoro, String> dataColumn;

	@FXML
	private TableColumn<OffertaLavoro, ButtonBar> azioniColumn;

	public InsertedOffersController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		offertaService = factory.getOffertaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/* Inizializzo le colonne della tabella */

		settoreColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OffertaLavoro, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<OffertaLavoro, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getSettore());
					}
				});
		salarioColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OffertaLavoro, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<OffertaLavoro, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getSalario());
					}
				});
		dataColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OffertaLavoro, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<OffertaLavoro, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getDataInserimento().toString());
					}
				});
		azioniColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OffertaLavoro, ButtonBar>, ObservableValue<ButtonBar>>() {
					@Override
					public ObservableValue<ButtonBar> call(CellDataFeatures<OffertaLavoro, ButtonBar> param) {
						final ButtonBar azioniButtonBar = new ButtonBar();
						final Button modificaButton = new Button();
						final Button eliminaButton = new Button();

						modificaButton.setText("Modifica");
						eliminaButton.setText("Elimina");
						modificaButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								dispatcher.renderView("edit_offer", param.getValue());
							}
						});
						eliminaButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									offertaService.removeOfferta(param.getValue());
									initializeData(azienda);
								} catch (BusinessExc e) {
									dispatcher.renderError(e);
								}
							}
						});
						azioniButtonBar.getButtons().addAll(modificaButton, eliminaButton);
						return new SimpleObjectProperty<ButtonBar>(azioniButtonBar);
					}
				});
	}

	@Override
	public void initializeData(Azienda azienda) {
		this.azienda = azienda;
		try {
			offerte = offertaService.findOfferteByAzienda(azienda);
			ObservableList<OffertaLavoro> offerteData = FXCollections.observableArrayList(offerte);
			insertedOffersTable.setItems(offerteData);
			settoreColumn.setSortType(TableColumn.SortType.ASCENDING);
			insertedOffersTable.getSortOrder().add(settoreColumn);
			insertedOffersTable.sort();
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}

	}
}
