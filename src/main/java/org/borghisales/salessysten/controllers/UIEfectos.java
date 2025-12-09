package org.borghisales.salessysten.controllers;

import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class UIEfectos {

    // Definimos los estilos (pues se reciclaran)
    private static final String VERDE_NORMAL = "-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";
    private static final String VERDE_HOVER  = "-fx-background-color: #4caf50; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";

    private static final String AZUL_NORMAL  = "-fx-background-color: #1565c0; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";
    private static final String AZUL_HOVER   = "-fx-background-color: #42a5f5; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";

    private static final String ROJO_NORMAL  = "-fx-background-color: #c62828; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";
    private static final String ROJO_HOVER   = "-fx-background-color: #ef5350; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";

    private static final String GRIS_NORMAL  = "-fx-background-color: #424242; -fx-text-fill: #aaa; -fx-background-radius: 5;";
    private static final String GRIS_HOVER   = "-fx-background-color: #616161; -fx-text-fill: white; -fx-background-radius: 5;";

    private static final String NARANJA_NORMAL = "-fx-background-color: transparent; -fx-text-fill: #ffa726; -fx-border-color: #fb8c00; -fx-border-radius: 5;";
    private static final String NARANJA_HOVER  = "-fx-background-color: #fb8c00; -fx-text-fill: white; -fx-border-color: #fb8c00; -fx-border-radius: 5;";

    // metodos**************************

    public static void styleButtonAdd(Button btn) {
        applyAnimation(btn, VERDE_NORMAL, VERDE_HOVER);
    }
    public static void styleButtonUpdate(Button btn) {
        applyAnimation(btn, AZUL_NORMAL, AZUL_HOVER);
    }
    public static void styleButtonDelete(Button btn) {
        applyAnimation(btn, ROJO_NORMAL, ROJO_HOVER);
    }
    public static void styleButtonGray(Button btn) {
        applyAnimation(btn, GRIS_NORMAL, GRIS_HOVER);
    }
    public static void styleButtonReturn(Button btn) {
        applyAnimation(btn, NARANJA_NORMAL, NARANJA_HOVER);
    }

    // logica**********************************

    private static void applyAnimation(Button btn, String normal, String hover) {
        if (btn == null) return;

        btn.setCursor(Cursor.HAND);
        btn.setStyle(normal);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(hover);
            scaleButton(btn, 1.05);
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(normal);
            scaleButton(btn, 1.0);
        });
    }

    private static void scaleButton(Button btn, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
}