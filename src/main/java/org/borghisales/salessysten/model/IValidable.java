package org.borghisales.salessysten.model;

public interface IValidable {
    String validarCampos();

    static boolean esCampoVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

}
