package org.borghisales.salessysten;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import org.borghisales.salessysten.controllers.MenuController;

import java.io.IOException;

import static org.borghisales.salessysten.controllers.MenuController.*;

public class Main extends Application {
    MenuController mc = new MenuController();
    @Override
    public void init() throws Exception {
        super.init();
        filePaths.put(MANAGEMENT_VIEW_FXML, MAIN_VIEW_FXML);
        filePaths.put(SELLER_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(PRODUCT_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(CUSTOMER_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(GENERATE_SALE_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(REPORT_VIEW_FXML, MANAGEMENT_VIEW_FXML);
    }

    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_VIEW_FXML));
        Scene scene = new Scene(loader.load(), 400, 500);
        stage.setTitle("Inicio de sesión");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}