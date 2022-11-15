package it.univaq.disim.oop.croissantmanager;

import it.univaq.disim.oop.croissantmanager.view.ViewDispatcher;
import it.univaq.disim.oop.croissantmanager.view.ViewException;
import javafx.application.Application;
import javafx.stage.Stage;

public class CroissantManager extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		/*
		 * Il compito di gestire le viste è demandato alla classe ViewDispatcher Viene
		 * invocato il metodo startupView della classe ViewDispatcher, il quale carica
		 * la vista di startup
		 */

		try {
			ViewDispatcher dispatcher = ViewDispatcher.getInstance();
			dispatcher.startupView(stage);
		}

		catch (ViewException e) {
			e.printStackTrace();
		}
	}
}
