package org.borghisales.salessysten.model;

public record Brand(int idBrand, String name) {
    @Override
    //regresa el nombre asignado a la marca ekisde
    public String toString() {
        return name;
    }
}