package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.domain.Candidatura;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class OffersController implements Initializable, DataInitializable<Lavoratore> {

	private Lavoratore lavoratore;
	private ViewDispatcher dispatcher;
	private OffertaService offertaService;

	@FXML
	private TextField searchTextField;

	@FXML
	private Button searchButton;

	@FXML
	private TableView<OffertaLavoro> offerteTable;

	@FXML
	private TableColumn<OffertaLavoro, String> aziendaColumn;

	@FXML
	private TableColumn<OffertaLavoro, String> settoreColumn;

	@FXML
	private TableColumn<OffertaLavoro, Float> localitaColumn;

	@FXML
	private TableColumn<OffertaLavoro, Button> azioniColumn;

	@FXML
	private Label errorLabel;

	public OffersController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		offertaService = factory.getOffertaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/* Inizializzo le colonne della tabella. */

		aziendaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OffertaLavoro, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<OffertaLavoro, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getAzienda().getRagione());
					}
				});
		settoreColumn.setCellValueFactory(new PropertyValueFactory<>("settore"));
		localitaColumn.setCellValueFactory(new PropertyValueFactory<>("localita"));
		azioniColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<OffertaLavoro, Button>, ObservableValue<Button>>() {
					@Override
					public ObservableValue<Button> call(CellDataFeatures<OffertaLavoro, Button> param) {
						final Button infoButton = new Button("Candidati");
						infoButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									Candidatura candidatura = new Candidatura();
									candidatura.setDataCandidatura(LocalDate.now());
									candidatura.setLavoratore(lavoratore);
									candidatura.setOfferta(param.getValue());
									offertaService.addCandidatura(candidatura);
									errorLabel.setText("Candidatura inviata!");
								} catch (BusinessExc e) {
									dispatcher.renderError(e);
								}
							}
						});
						return new SimpleObjectProperty<Button>(infoButton);
					}
				});

	}

	@Override
	public void initializeData(Lavoratore lavoratore) {
		this.lavoratore = lavoratore;
		try {
			Set<OffertaLavoro> offerte = offertaService.findAllOfferte();
			ObservableList<OffertaLavoro> offerteData = FXCollections.observableArrayList(offerte);
			offerteTable.setItems(offerteData);
			aziendaColumn.setSortType(TableColumn.SortType.ASCENDING);
			offerteTable.getSortOrder().add(aziendaColumn);
			offerteTable.sort();
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

	/* Metodo invocato al click sul bottone "Cerca" */

	@FXML
	public void searchButtonAction(ActionEvent event) {
		Set<OffertaLavoro> offerte = new HashSet<>();
		try {
			if (searchTextField.getText().isEmpty()) {
				offerte = offertaService.findAllOfferte();
			} else {
				offerte = offertaService.findOffertaByLocalitaOrKeyword(searchTextField.getText());
			}
			ObservableList<OffertaLavoro> offerteData = FXCollections.observableArrayList(offerte);
			offerteTable.setItems(offerteData);
			aziendaColumn.setSortType(TableColumn.SortType.ASCENDING);
			offerteTable.getSortOrder().add(aziendaColumn);
			offerteTable.sort();
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

}
