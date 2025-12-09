package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController extends MenuController implements Initializable {



    @FXML
    private ImageView email;
    @FXML
    private ImageView telefono;
    @FXML
    private ImageView uadyy;
    @FXML
    private ImageView github;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image img = new Image(getClass().getResource("/images/email.png").toString());
        email.setImage(img);

        Image img2 = new Image(getClass().getResource("/images/telefono.png").toString());
        telefono.setImage(img2);

        Image img3 = new Image(getClass().getResource("/images/uadyy.jpeg").toString());
        uadyy.setImage(img3);

        Image img4 = new Image(getClass().getResource("/images/github.png").toString());
        github.setImage(img4);




    }


}
