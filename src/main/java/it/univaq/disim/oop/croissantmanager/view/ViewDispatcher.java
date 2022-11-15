
package it.univaq.disim.oop.croissantmanager.view;

import java.io.IOException;

import it.univaq.disim.oop.croissantmanager.controller.DataInitializable;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.Utente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewDispatcher {
	private static final String FXML_SUFFIX = ".fxml";
	private static final String RESOURCE_BASE = "/views/";
	private static ViewDispatcher instance = new ViewDispatcher();

	private ViewDispatcher() {
	}

	public static ViewDispatcher getInstance() {
		return instance;
	}

	private Stage stage;

	/* I seguenti 4 metodi si occupano del passaggio da una view a l'altra. */

	public void startupView(Stage stage) throws ViewException {
		this.stage = stage;
		Parent startupView = loadView("startup").getView();
		Scene scene = new Scene(startupView);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void companyRegView() {
		try {
			View<Azienda> companyRegView = loadView("register_company");
			Scene scene = new Scene((BorderPane) companyRegView.getView());
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	public void userRegView() {
		try {
			View<Lavoratore> userRegView = loadView("register_user");
			Scene scene = new Scene((BorderPane) userRegView.getView());
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	public void userSkillsView(Lavoratore lavoratore) {
		try {
			View<Lavoratore> userSkillsView = loadView("skills");
			DataInitializable<Lavoratore> controller = userSkillsView.getController();
			controller.initializeData(lavoratore);
			Scene scene = new Scene((AnchorPane) userSkillsView.getView());
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	private BorderPane layout;

	/*
	 * Il seguente metodo passa al "main_layout" al seguito di un login andato a
	 * buon fine
	 */

	public void loggedIn(Utente utente) {
		try {
			View<Utente> mainLayoutView = loadView("main_layout");
			DataInitializable<Utente> mainLayoutController = mainLayoutView.getController();
			mainLayoutController.initializeData(utente);
			layout = (BorderPane) mainLayoutView.getView();
			renderView("welcome", utente);
			layout.setId("borderpane");
			Scene scene = new Scene(layout);
			scene.getStylesheets().add(getClass().getResource(RESOURCE_BASE + "styles.css").toExternalForm());
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	/* Il seguente metodo torna alla schermata di startup al seguito di un logout */

	public void logout() {
		try {
			Parent startupView = loadView("startup").getView();
			Scene scene = new Scene(startupView);
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	/*
	 * Questo metodo si occupa di renderizzare altre schermate all'interno del pane
	 * centrale del main_layout.
	 */

	public <T> void renderView(String viewName, T data) {
		try {
			View<T> view = loadView(viewName);
			DataInitializable<T> controller = view.getController();
			controller.initializeData(data);
			layout.setCenter(view.getView());
		} catch (ViewException e) {
			renderError(e);
		}
	}

	public void renderError(Exception e) {
		e.printStackTrace();
		System.exit(1);
	}

	/*
	 * Il seguente metodo si occupa di caricare una vista, restituendo un oggetto di
	 * tipo View<T>
	 */

	private <T> View<T> loadView(String view) throws ViewException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE + view + FXML_SUFFIX));
			Parent parent = (Parent) loader.load();
			return new View<>(parent, loader.getController());
		} catch (IOException e) {
			throw new ViewException(e);
		}
	}
}