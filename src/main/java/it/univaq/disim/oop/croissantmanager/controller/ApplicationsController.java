package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Candidatura;
import it.univaq.disim.oop.croissantmanager.domain.Competenza;
import it.univaq.disim.oop.croissantmanager.domain.Messaggio;
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

public class ApplicationsController implements Initializable, DataInitializable<Azienda> {

	private Azienda azienda;
	private ViewDispatcher dispatcher;
	private OffertaService offertaService;

	@FXML
	private TableView<Candidatura> applicationsTable;

	@FXML
	private TableColumn<Candidatura, String> nomeColumn;

	@FXML
	private TableColumn<Candidatura, String> settoreColumn;

	@FXML
	private TableColumn<Candidatura, String> dataColumn;

	@FXML
	private TableColumn<Candidatura, Integer> affinitaColumn;

	@FXML
	private TableColumn<Candidatura, ButtonBar> azioniColumn;

	@FXML
	private Label errorLabel;

	@FXML
	private Button backButton;

	public ApplicationsController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		offertaService = factory.getOffertaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Candidatura, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getLavoratore().getNome() + " "
								+ param.getValue().getLavoratore().getCognome());
					}
				});
		settoreColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Candidatura, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getOfferta().getSettore());
					}
				});
		dataColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Candidatura, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getDataCandidatura().toString());
					}
				});
		affinitaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, Integer>, ObservableValue<Integer>>() {
					@Override
					public ObservableValue<Integer> call(CellDataFeatures<Candidatura, Integer> param) {
						Set<Competenza> compOfferta = new HashSet<>(
								param.getValue().getOfferta().getCompetenzeRichieste());
						Set<Competenza> compLavoratore = new HashSet<>(
								param.getValue().getLavoratore().getCompetenzePossedute());
						compLavoratore.retainAll(compOfferta);
						float compOffertaSize = compOfferta.size();
						float compLavoratoreSize = compLavoratore.size();
						return new SimpleObjectProperty<Integer>(
								Integer.valueOf((int) ((compLavoratoreSize / compOffertaSize) * 100.0f)));
					}
				});
		azioniColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Candidatura, ButtonBar>, ObservableValue<ButtonBar>>() {
					@Override
					public ObservableValue<ButtonBar> call(CellDataFeatures<Candidatura, ButtonBar> param) {
						final ButtonBar azioniButtonBar = new ButtonBar();
						final Button contattaButton = new Button();

						contattaButton.setText("Contatta");
						contattaButton.setPrefHeight(10);
						contattaButton.setPrefWidth(40);
						contattaButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									Messaggio messaggio = new Messaggio();
									messaggio.setAzienda(azienda);
									messaggio.setLavoratore(param.getValue().getLavoratore());
									dispatcher.renderView("send_message", messaggio);
								} catch (Exception e) {
									dispatcher.renderError(e);
								}
							}
						});
						azioniButtonBar.getButtons().add(contattaButton);
						return new SimpleObjectProperty<ButtonBar>(azioniButtonBar);
					}
				});
	}

	@Override
	public void initializeData(Azienda azienda) {
		this.azienda = azienda;
		try {
			List<Candidatura> result = new ArrayList<>();
			List<Candidatura> offerte = offertaService.findAllCandidature();
			Iterator<Candidatura> iterator = offerte.iterator();
			while (iterator.hasNext()) {
				Candidatura cand = iterator.next();
				if (cand.getOfferta().getAzienda().getRagione().equals(azienda.getRagione())) {
					result.add(cand);
				}
			}
			ObservableList<Candidatura> offerteData = FXCollections.observableArrayList(result);
			applicationsTable.setItems(offerteData);
			affinitaColumn.setSortType(TableColumn.SortType.DESCENDING);
			applicationsTable.getSortOrder().add(affinitaColumn);
			applicationsTable.sort();
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

}
