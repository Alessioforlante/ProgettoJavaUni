package it.univaq.disim.oop.croissantmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.CroissantManagerBusinessFactory;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Messaggio;
import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SendMessageController implements Initializable, DataInitializable<Messaggio> {

	private Messaggio messaggio;
	private ViewDispatcher dispatcher;
	private UtenteService utenteService;

	public SendMessageController() {
		dispatcher = ViewDispatcher.getInstance();
		CroissantManagerBusinessFactory factory = CroissantManagerBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
	}

	@FXML
	private TextField messageTextField;

	@FXML
	private Button sendButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void initializeData(Messaggio messaggio) {
		this.messaggio = messaggio;
	}

	@FXML
	public void sendButtonAction(ActionEvent event) {
		try {
			messaggio.setTesto(messageTextField.getText());
			utenteService.sendMessaggio(messaggio, messaggio.getLavoratore());
			dispatcher.renderView("applications", messaggio.getAzienda());
		} catch (BusinessExc e) {
			dispatcher.renderError(e);
		}
	}

}
