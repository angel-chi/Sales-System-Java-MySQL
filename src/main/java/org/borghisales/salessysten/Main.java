package org.borghisales.salessysten;

import javafx.application.Application;
import javafx.stage.Stage;
import org.borghisales.salessysten.controllers.MenuController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MenuController mc = new MenuController();
        mc.openNewStage(MenuController.MAIN_VIEW_FXML, "Inicio de Sesion");
    }

    public static void main(String[] args) {
        launch();
    }
}