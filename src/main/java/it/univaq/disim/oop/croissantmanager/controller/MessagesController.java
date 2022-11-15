package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MessagesController implements Initializable, DataInitializable<Lavoratore> {

	private UtenteService utenteService;
	private ViewDispatcher dispatcher;
	
	@FXML
	private VBox messagesBox;

	public MessagesController() {
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
		dispatcher = ViewDispatcher.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void initializeData(Lavoratore lavoratore) {
		try {
			Set<StringBuilder> messaggi = utenteService.findAllMessaggi(lavoratore);
			for (StringBuilder messaggio : messaggi) {
				Text messaggioText = new Text(messaggio.toString());
				messagesBox.getChildren().add(messaggioText);
			}
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

}
