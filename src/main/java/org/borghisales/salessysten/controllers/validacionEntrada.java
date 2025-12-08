package org.borghisales.salessysten.controllers;

/*Interfaz para validar campos de texto o valores vacíos. Anteriormente era posible añadir vendedores con
campo de nombre vacío a la tabla y cuando se intentaba agregar un producto no había un mensaje de advertencia.*/

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public interface validacionEntrada {
    default void mostrarAdvertencia (String mensaje){
        Alert alerta=new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText("Información");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    //Metodo para detectar una cadena vacía. Puede comprobar si el contenido es vacio o si hay espacios en blanco
    default boolean campoVacio(TextField contenido){
        return contenido == null || contenido.getText().trim().isEmpty();
    }


}
