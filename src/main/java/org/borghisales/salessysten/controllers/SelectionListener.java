package org.borghisales.salessysten.controllers;

/**
 * Interfaz  para pasar datos seleccionados desde un controlador a otro
 */
@FunctionalInterface
public interface SelectionListener<T> {
    void onItemSelected(T item);
}
