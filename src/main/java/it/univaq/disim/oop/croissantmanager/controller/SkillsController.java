package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Competenza;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

public class SkillsController implements Initializable, DataInitializable<Lavoratore> {

	private ViewDispatcher dispatcher;
	private Lavoratore lavoratore;
	private UtenteService utenteService;

	@FXML
	private ListView<CheckBox> competenzeListView;

	@FXML
	private Button completeButton;

	public SkillsController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for (Competenza competenza : Competenza.values()) {
			CheckBox checkbox = new CheckBox();
			checkbox.setText(competenza.toString());
			competenzeListView.getItems().add(checkbox);
		}
	}

	@Override
	public void initializeData(Lavoratore lavoratore) {
		this.lavoratore = lavoratore;
	}

	@FXML
	public void completeButtonAction(ActionEvent event) {
		try {
			Set<Competenza> competenze = new HashSet<>();
			for (CheckBox checkbox : competenzeListView.getItems()) {
				if (checkbox.isSelected()) {
					// TODO: sistemare la seguente linea di codice utilizzando il metodo valueOf
					competenze.add(Competenza.getCompetenzaByText(checkbox.getText()));
				}
			}
			lavoratore.setCompetenzePossedute(competenze);
			utenteService.registerAsWorker(lavoratore);
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
		dispatcher.logout();
	}
}
